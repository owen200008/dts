import mockjs from 'mockjs';
import { getNotices } from './mock/notices';

import * as dataSource from './mock/dataSource';
import * as task from './mock/task';
import * as pipeline from './mock/pipeline';
import { getUsers } from './mock/user';
import { getPlatform, platformEnum, userLogin } from './mock/platform'
import { format, delay } from 'roadhog-api-doc';

// 是否禁用代理
const noProxy = process.env.NO_PROXY === 'true';

// 代码中会兼容本地 service mock 以及部署站点的静态数据
const proxy = {
  'GET /platform': {
    $body: getPlatform,
  },
  'GET /platform/enum': {
    $body: platformEnum,
  },
  'GET /platform/login': {
    $body: userLogin,
  },
  'GET /dashboardUser': {
    $params: {
      userName: 'ADMIN',
      currentPage: 1,
      pageSize: 10,
    },
    $body: getUsers(),
  },

  'GET /dashboardUser/1': {
    "code": 200,
    "message": "detail dashboard user success",
    "data": {
      "id": "1",
      "userName": "admin",
      "password": "123456",
      "role": 1,
      "enabled": false,
      "dateCreated": "2018-06-23 15:12:22",
      "dateUpdated": "2018-06-23 15:12:23"
    }
  },
  'POST /dashboardUser': {
    $body: {
      code: 200
    },
  },
  'PUT /dashboardUser/1': {
    $body: {
      code: 200
    },
  },
  'POST /dashboardUser/delete': {
    $body: {
      code: 200
    },
  },



  'GET /plugin/1': {
    "code": 200,
    "message": "detail dashboard user success",
    "data": {
      "id": "1",
      "name": "admin",
      "enabled": false,
    }
  },
  'POST /plugin': {
    $body: {
      code: 200
    },
  },
  'PUT /plugin/1': {
    $body: {
      code: 200
    },
  },
  'POST /plugin/delete': {
    $body: {
      code: 200
    },
  },
  'GET /api/notices': getNotices,
  'GET /api/500': (req, res) => {
    res.status(500).send({
      timestamp: 1513932555104,
      status: 500,
      error: 'error',
      message: 'error',
      path: '/base/category/list',
    });
  },
  'GET /api/404': (req, res) => {
    res.status(404).send({
      timestamp: 1513932643431,
      status: 404,
      error: 'Not Found',
      message: 'No message available',
      path: '/base/category/list/2121212',
    });
  },
  'GET /api/403': (req, res) => {
    res.status(403).send({
      timestamp: 1513932555104,
      status: 403,
      error: 'Unauthorized',
      message: 'Unauthorized',
      path: '/base/category/list',
    });
  },
  'POST /dts/entity/list': {
    $body: dataSource.listItems,
  },
  'POST /dts/entity/add': {
    $body: {
      code: 200
    },
  },
  'POST /dts/entity/delete': {
    $body: {
      code: 200
    },
  },
  'POST /dts/entity/get': {
    $body: dataSource.getItem,
  },

  'POST /dts/task/list': {
    $body: task.listItems,
  },
  'POST /dts/task/add': {
    $body: {
      code: 200
    },
  },
  'POST /dts/task/delete': {
    $body: {
      code: 200
    },
  },
  'POST /dts/task/switch': {
    $body: {
      code: 200
    },
  },
  'POST /dts/pipeline/list': {
    $body: pipeline.listItems,
  },
  'POST /dts/pipeline/add': {
    $body: {
      "code": 200,
      "msg": "success",
      "data": {
        "pipelineId": 1223
      }
    },
  },
};

export default (noProxy ? {} : delay(proxy, 1000));
