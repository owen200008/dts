import { message } from "antd";
import { routerRedux } from "dva/router";
import {
  addItem,
  deleteItem,
  getItem,
  listItems,
  listItemsById
} from "../services/region";

import * as pipeline from '../services/pipeline';

export default {
  namespace: "region",

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

        let promiseArr = (data.map(async item => {
          item.key = item.id;
          let resp = await pipeline.getItem({ pipelineId: item.pipelineId });
          item.pipelineName = resp?.data?.name;
          return item;
        }));
        let dataList = yield call(async () => { let result = await Promise.all(promiseArr); return result; });
        yield put({
          type: "saveList",
          payload: {
            total,
            dataList
          }
        });
      }
    },
    * fetchById(params, { call }) {
      const { payload, callback } = params;
      const json = yield call(listItemsById, payload);
      if (json.code === 200) {
        callback(json.data)
      }

    },
    * fetchItem(params, { call }) {
      const { payload, callback } = params;
      const json = yield call(getItem, payload);
      if (json.code === 200) {
        const plugin = json.data;
        callback(plugin);
      }
    },
    * add(params, { call, put }) {
      const { payload, callback, fetchValue } = params;
      const { sourceRegion, targetRegion } = payload;

      yield call(addItem, sourceRegion);
      const json = yield call(addItem, targetRegion);
      if (json.code === 200) {
        message.success("添加成功");
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
