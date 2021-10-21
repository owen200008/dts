import { message } from "antd";
import { routerRedux } from "dva/router";
import {
  addItem,
  deleteItem,
  getItem,
  listItems,
  listItemsById
} from "../services/pair";
import * as pipeline from '../services/pipeline';
import * as sourceData from '../services/sourceData';
import * as targetData from '../services/targetData';

export default {
  namespace: "pair",

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
      if (json.code === '200') {
        let { total, data } = json.data;
        let promiseArr = (data.map(async item => {
          item.key = item.id;
          let [pipeResp, sourceResp, targeResp] = await Promise.all([
            pipeline.getItem({ pipelineId: item.pipelineId }),
            sourceData.getItem({ sourceId: item.sourceDatamediaId }),
            targetData.getItem({ targetId: item.targetDatamediaId }),
          ]);
          item.pipelineName = pipeResp?.data?.name;
          item.sourceData = [sourceResp?.data?.name, sourceResp?.data?.namespace, sourceResp?.data?.table].join('->');
          item.targetData = [targeResp?.data?.name, targeResp?.data?.namespace, targeResp?.data?.table].join('->');
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
      if (json.code === '200') {
        callback(json.data)
      }

    },
    * fetchItem(params, { call }) {
      const { payload, callback } = params;
      const json = yield call(getItem, payload);
      if (json.code === "200") {
        const plugin = json.data;
        callback(plugin);
      }
    },
    * add(params, { call, put }) {
      const { payload, callback, fetchValue } = params;
      const json = yield call(addItem, payload);
      if (json.code === '200') {
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
      if (json.code === '200') {
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
