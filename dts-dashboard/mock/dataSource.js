export const getItem = (req, res) => {
  res.json({
    "code": '200',
    "message": "success",
    "data": {
      "id": 122,
      "name": "canary数据源",
      "type": "MYSQL",
      "encode": "",
      "slaveId": 1222,
      "url": "jdbc:mysql://global-db.blurams.vip:3306/u_dts?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC",
      "driver": "com.mysql.cj.jdbc.Driver",
      "username": "dev",
      "password": "Aa123456",
      "mysql": ""
    }
  });
}

export const listItems = (req, res) => {
  res.json({
    "code": '200',
    "message": "success",
    "data": {
      "pageSize": 12,
      "totalCount": 150,
      "entityList": [
        {
          "id": 121,
          "name": "canary数据源111",
          "type": "MYSQL",
          "encode": "",
          "slaveId": 1222,
          "url": "jdbc:mysql://global-db.blurams.vip:3306/u_dts?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC",
          "driver": "com.mysql.cj.jdbc.Driver",
          "username": "dev",
          "password": "Aa123456",
          "mysql": ""
        },
        {
          "id": 122,
          "name": "canary数据源222",
          "type": "MYSQL",
          "encode": "",
          "slaveId": 1222,
          "url": "jdbc:mysql://global-db.blurams.vip:3306/u_dts?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC",
          "driver": "com.mysql.cj.jdbc.Driver",
          "username": "dev",
          "password": "Aa123456",
          "mysql": ""
        },
        {
          "id": 123,
          "name": "canary数据源333",
          "type": "MYSQL",
          "encode": "",
          "slaveId": 1222,
          "url": "jdbc:mysql://global-db.blurams.vip:3306/u_dts?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC",
          "driver": "com.mysql.cj.jdbc.Driver",
          "username": "dev",
          "password": "Aa123456",
          "mysql": ""
        },
        {
          "id": 124,
          "name": "canary数据源444",
          "type": "MYSQL",
          "encode": "",
          "slaveId": 1222,
          "url": "jdbc:mysql://global-db.blurams.vip:3306/u_dts?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC",
          "driver": "com.mysql.cj.jdbc.Driver",
          "username": "dev",
          "password": "Aa123456",
          "mysql": ""
        },
        {
          "id": 125,
          "name": "canary数据源555",
          "type": "MYSQL",
          "encode": "",
          "slaveId": 1222,
          "url": "jdbc:mysql://global-db.blurams.vip:3306/u_dts?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC",
          "driver": "com.mysql.cj.jdbc.Driver",
          "username": "dev",
          "password": "Aa123456",
          "mysql": ""
        },
        {
          "id": 126,
          "name": "canary数据源666",
          "type": "MYSQL",
          "encode": "",
          "slaveId": 1222,
          "url": "jdbc:mysql://global-db.blurams.vip:3306/u_dts?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC",
          "driver": "com.mysql.cj.jdbc.Driver",
          "username": "dev",
          "password": "Aa123456",
          "mysql": ""
        },
        {
          "id": 127,
          "name": "canary数据源777",
          "type": "MYSQL",
          "encode": "",
          "slaveId": 1222,
          "url": "jdbc:mysql://global-db.blurams.vip:3306/u_dts?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC",
          "driver": "com.mysql.cj.jdbc.Driver",
          "username": "dev",
          "password": "Aa123456",
          "mysql": ""
        },


      ],
    },
  });
};
export default {
  listItems,
  getItem
};
