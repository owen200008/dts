import { message } from "antd";
import { routerRedux } from "dva/router";
import {
  addItem,
  deleteItem,
  getItem,
  listItems,
  pairAddItem,
  regionAddItem,
  regionGetItem,
  entityGetItem
} from "../services/pipeline";

export default {
  namespace: "pipeline",

  state: {
    dataList: [],
    total: 0
  },

  effects: {
    * redirect({ location }, { put }) {
      yield put(routerRedux.push(location));
    },
    * fetch(params, { call, put }) {
      const { payload } = params;
      const json = yield call(listItems, payload);
      if (json.code === 200) {
        let { totalCount, entityList } = json.data;
        let dataList = entityList.map(item => {
          item.key = item.id;
          return item;
        });
        yield put({
          type: "saveList",
          payload: {
            total: totalCount,
            dataList
          }
        });
      }
    },
    * fetchItem(params, { call }) {
      const { payload, callback } = params;
      const json = yield call(getItem, payload);
      if (json.code === 200) {
        callback(json.data);
      }
    },
    * fetchRegionItem(params, { call }) {
      const { payload, callback } = params;
      const json = yield call(regionGetItem, payload);
      if (json.code === 200) {
        callback(json.data);
      }
    },
    * fetchEntityItem(params, { call }) {
      const { payload, callback } = params;
      const json = yield call(entityGetItem, payload);
      if (json.code === 200) {
        callback(json.data);
      }
    },
    * add(params, { call, put }) {
      const { payload, callback, fetchValue } = params;
      const json = yield call(addItem, payload);
      if (json.code === 200) {
        message.success("添加成功");
        callback();
        yield put({ type: "reload", fetchValue });
      } else {
        message.warn(json.msg || json.data);
      }
    },

    * delete(params, { call, put }) {
      const { payload, fetchValue, callback } = params;
      const json = yield call(deleteItem, payload);
      if (json.code === 200) {
        message.success("删除成功");
        callback();
        yield put({ type: "reload", fetchValue });
      } else {
        message.warn(json.msg || json.data);
      }
    },

    * pairAdd(params, { call, put }) {
      const { payload, fetchValue, callback } = params;
      const json = yield call(pairAddItem, payload);
      if (json.code === 200) {
        message.success("添加成功");
        callback();
        yield put({ type: "reload", fetchValue });
      } else {
        message.warn(json.msg || json.data);
      }
    },

    * regionAdd(params, { call, put }) {
      const { payload, fetchValue, callback } = params;
      const json = yield call(regionAddItem, payload);
      if (json.code === 200) {
        message.success("添加成功");
        callback();
        yield put({ type: "reload", fetchValue });
      } else {
        message.warn(json.msg || json.data);
      }
    },


    * reload(params, { put }) {
      const { fetchValue: payload } = params;
      yield put({ type: "fetch", payload });
    }
  },

  reducers: {
    saveList(state, { payload }) {
      return {
        ...state,
        dataList: payload.dataList,
        total: payload.total
      };
    },
    updataItem(state, { payload }) {
      let dataList = state.dataList;
      dataList = dataList.map((item) => {
        if (item.id === payload.id) {
          item.enabled = payload.enabled;
        }
        return item;
      })
      return {
        ...state,
        dataList,
      };
    }
  }
};
