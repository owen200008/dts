import { message } from "antd";
import { routerRedux } from "dva/router";
import {
  addItem,
  deleteItem,
  getItem,
  updateItem,
  listItems,
  getProperty,
} from "../services/dataSource";

export default {
  namespace: "dataSource",

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
        let { total, data = [] } = json.data || {};
        let dataList = data.map(item => {
          item.key = item.id;
          return item;
        });
        yield put({
          type: "saveList",
          payload: {
            total,
            dataList
          }
        });
      }
    },
    

    * fetchProperty(params, { call }) {
      const { payload, callback } = params;
      const json = yield call(getProperty, payload);
      if (json.code === 200) {
        callback(json.data);
      }
    },

    * fetchItem(params, { call }) {
      const { payload, callback } = params;
      const json = yield call(getItem, payload);
      if (json.code === 200) {
        callback(json.data);
      }
    },
    * add(params, { call, put }) {
      const { payload, callback, fetchValue } = params;
      const json = yield call(addItem, payload);
      if (json.code === 200) {
        message.success("添加成功");
        callback(json.data);
        if (fetchValue) yield put({ type: "reload", fetchValue });
      } else {
        message.warn(json.msg || json.data);
      }
    },
    * update(params, { call, put }) {
      const { payload, callback, fetchValue } = params;
      const json = yield call(updateItem, payload);
      if (json.code === 200) {
        message.success("更新成功");
        callback(json.data);
        if (fetchValue) yield put({ type: "reload", fetchValue });
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
        if (fetchValue) yield put({ type: "reload", fetchValue });
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
