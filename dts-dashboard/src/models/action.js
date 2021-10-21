import { message } from "antd";
import { routerRedux } from "dva/router";
import {
  getAllActions,
  updateAction,
  deleteAction,
  addAction,
} from "../services/action";
import {
  getAllPlugins
} from "../services/api";

export default {
  namespace: "action",

  state: {
    actionList: [],
    total: 0
  },

  effects: {
    * redirect({ location }, { put }) {
      yield put(routerRedux.push(location));
    },
    * fetch(params, { call, put }) {
      const { payload } = params;
      const json = yield call(getAllActions, payload);
      if (json.code === '200') {
        let { page, dataList } = json.data;

        let arr = [];
        dataList.forEach(({pluginId}) => {
          pluginId && arr.push(getAllPlugins({id: pluginId}));
        });
        let respArr = yield Promise.all(arr);
        let pluginData = {};

        respArr.forEach(item => {
          if (item.code === '200') {
            let list = item.data.dataList || [];
            list.forEach(o => {              
                pluginData[o.id] = o.name;
            })
          }
        });

        dataList = dataList.map(item => {
          item.pluginName = pluginData[item.pluginId] || '';
          item.key = item.id;
          return item;
        });
        yield put({
          type: "saveActions",
          payload: {
            total: page.totalCount,
            dataList
          }
        });
      }
    },

    * add(params, { call, put }) {
      const { payload, callback, fetchValue } = params;
      const json = yield call(addAction, payload);
      if (json.code === '200') {
        message.success("添加成功");
        callback();
        yield put({ type: "reload", fetchValue });
      } else {
        message.warn(json.msg || json.data);
      }
    },
    * changeStatus({ payload }, { call, put }) {
      const json = yield call(updateAction, payload);
      if (json.code === '200') {
        message.success("修改成功");
        yield put({
          type: "updataActions",
          payload,
        });
      } else {
        message.warn(json.msg || json.data);
      }
    },
    * delete(params, { call, put }) {
      const { payload, fetchValue, callback } = params;
      const { id } = payload;
      const json = yield call(deleteAction, { id });
      if (json.code === '200') {
        message.success("删除成功");
        callback();
        yield put({ type: "reload", fetchValue });
      } else {
        message.warn(json.msg || json.data);
      }
    },
    * update(params, { call, put }) {
      const { payload, callback, fetchValue } = params;
      const json = yield call(updateAction, payload);
      if (json.code === '200') {
        message.success("修改成功");
        callback();
        yield put({ type: "reload", fetchValue });
      } else {
        message.warn(json.msg || json.data);
      }
    },
    * reload(params, { put }) {
      const { fetchValue: payload } = params;
      yield put({ type: "fetch", payload });
    },
  },

  reducers: {
    saveActions(state, { payload }) {
      return {
        ...state,
        actionList: payload.dataList,
        total: payload.total
      };
    },
    updataActions(state, { payload }) {
      let actionList = state.actionList;
      actionList = actionList.map((item) => {
        if (item.id === payload.id) {
          item.enabled = payload.enabled;
        }
        return item;
      })
      return {
        ...state,
        actionList,
      };
    }
  }
};
