import { message } from "antd";
import { routerRedux } from "dva/router";
import {
  addItem,
  deleteItem,
  getItem,
  listItems,
  updateItem
} from "../services/regionPipe";

import * as pipeline from '../services/pipeline';
import * as region from '../services/region';

export default {
  namespace: "regionPipe",

  state: {
    dataList: [],
    total: 0
  },

  effects: {
    * redirect({ location }, { put }) {
      yield put(routerRedux.push(location));
    },
    * fetch(params, { call, put }) {
      const { payload, callback } = params;
      const json = yield call(listItems, payload);
      if (json.code === 200) {
        let { total, data = [] } = json.data || {};

        let promiseArr = (data.map(async item => {
          item.key = item.id;
          let [pipeResp, regionResp] = await Promise.all([
            pipeline.getItem({ pipelineId: item.pipelineId }),
            region.getItem({ regionId: item.regionId }),
          ]);
          item.pipelineName = pipeResp?.data?.name;
          item.region = regionResp?.data?.region;
          return item;
        }));
        let dataList = yield call(async () => {
          if (!promiseArr.lengnth) return [];
          let result = await Promise.all(promiseArr);
          return result;
        });
        if (callback) callback(dataList);
        yield put({
          type: "saveList",
          payload: {
            total,
            dataList
          }
        });
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
