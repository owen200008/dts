export const getPlatform = (req, res) => {
  res.json({
    "code": '200',
    "message": null,
    "data": {
      "matchModeEnums": [{
        "code": 0,
        "name": "and",
        "support": true,
      }, {
        "code": 1,
        "name": "or",
        "support": true,
      }],
      "wafEnums": [{
        "code": 0,
        "name": "reject",
        "support": true,
      }, {
        "code": 1,
        "name": "allow",
        "support": true,
      }],
      "pluginEnums": [{
        "code": 1,
        "name": "global",
        "support": true,
      }, {
        "code": 2,
        "name": "sign",
        "support": true,
      }, {
        "code": 10,
        "name": "waf",
        "support": true,
      }, {
        "code": 20,
        "name": "rate_limiter",
        "support": true,
      }, {
        "code": 30,
        "name": "rewrite",
        "support": true,
      }, {
        "code": 40,
        "name": "redirect",
        "support": true,
      }, {
        "code": 50,
        "name": "divide",
        "support": true,
      }, {
        "code": 60,
        "name": "dubbo",
        "support": true,
      }, {
        "code": 70,
        "name": "springCloud",
        "support": true,
      }, {
        "code": 80,
        "name": "monitor",
        "support": true,
      }],
      "selectorTypeEnums": [{
        "code": 0,
        "name": "full flow",
        "support": true,
      }, {
        "code": 1,
        "name": "custom flow",
        "support": true,
      }],
      "rpcTypeEnums": [{
        "code": null,
        "name": "http",
        "support": true,
      }, {
        "code": null,
        "name": "dubbo",
        "support": true,
      }, {
        "code": null,
        "name": "springCloud",
        "support": true,
      }, {
        "code": null,
        "name": "motan",
        "support": false,
      }, {
        "code": null,
        "name": "grpc",
        "support": false,
      }],
      "operatorEnums": [{
        "code": null,
        "name": "match",
        "support": true,
      }, {
        "code": null,
        "name": "=",
        "support": true,
      }, {
        "code": null,
        "name": ">",
        "support": false,
      }, {
        "code": null,
        "name": "<",
        "support": false,
      }, {
        "code": null,
        "name": "like",
        "support": true,
      }],
      "paramTypeEnums": [{
        "code": null,
        "name": "post",
        "support": false,
      }, {
        "code": null,
        "name": "uri",
        "support": false,
      }, {
        "code": null,
        "name": "query",
        "support": false,
      }, {
        "code": null,
        "name": "host",
        "support": true,
      }, {
        "code": null,
        "name": "ip",
        "support": true,
      }, {
        "code": null,
        "name": "header",
        "support": true,
      }],
      "pluginTypeEnums": [{
        "code": null,
        "name": "before",
        "support": true,
      }, {
        "code": null,
        "name": "function",
        "support": true,
      }, {
        "code": null,
        "name": "last",
        "support": true,
      }],
      "loadBalanceEnums": [{
        "code": 1,
        "name": "hash",
        "support": true,
      }, {
        "code": 2,
        "name": "random",
        "support": true,
      }, {
        "code": 3,
        "name": "roundRobin",
        "support": true,
      }],
      "httpMethodEnums": [{
        "code": null,
        "name": "get",
        "support": true,
      }, {
        "code": null,
        "name": "post",
        "support": true,
      }, {
        "code": null,
        "name": "put",
        "support": false,
      }, {
        "code": null,
        "name": "delete",
        "support": false,
      }],
      "serializeEnums": [{
        "code": null,
        "name": "jdk",
        "support": true,
      }, {
        "code": null,
        "name": "kryo",
        "support": true,
      }, {
        "code": null,
        "name": "hessian",
        "support": true,
      }, {
        "code": null,
        "name": "protostuff",
        "support": true,
      }],
    },
  });
};
export const userLogin = {
  "code": '200',
  "message": null,
  "data": {}
};

export const platformenum = {"code": '200',"message":null,"data":{"matchModeEnums":[{"code":0,"name":"and","support":true},{"code":1,"name":"or","support":true}],"wafEnums":[{"code":0,"name":"reject","support":true},{"code":1,"name":"allow","support":true}],"redisModeEnums":[{"code":null,"name":"cluster","support":true},{"code":null,"name":"sentinel","support":true},{"code":null,"name":"Standalone","support":true}],"pluginEnums":[{"code":1,"name":"global","support":true},{"code":2,"name":"sign","support":true},{"code":10,"name":"waf","support":true},{"code":20,"name":"rate_limiter","support":true},{"code":30,"name":"rewrite","support":true},{"code":40,"name":"redirect","support":true},{"code":50,"name":"divide","support":true},{"code":51,"name":"webSocket","support":true},{"code":60,"name":"dubbo","support":true},{"code":70,"name":"springCloud","support":true},{"code":80,"name":"monitor","support":true},{"code":100,"name":"response","support":true}],"selectorTypeEnums":[{"code":0,"name":"全流量","support":true},{"code":1,"name":"自定义流量","support":true}],"rpcTypeEnums":[{"code":null,"name":"http","support":true},{"code":null,"name":"dubbo","support":true},{"code":null,"name":"websocket","support":true},{"code":null,"name":"springCloud","support":true}],"operatorEnums":[{"code":null,"name":"match","support":true},{"code":null,"name":"=","support":true},{"code":null,"name":"regEx","support":true},{"code":null,"name":"like","support":true}],"paramTypeEnums":[{"code":null,"name":"self","support":true},{"code":null,"name":"query","support":true},{"code":null,"name":"host","support":true},{"code":null,"name":"ip","support":true},{"code":null,"name":"header","support":true}],"pluginTypeEnums":[{"code":null,"name":"before","support":true},{"code":null,"name":"function","support":true},{"code":null,"name":"last","support":true}],"loadBalanceEnums":[{"code":1,"name":"hash","support":true},{"code":2,"name":"random","support":true},{"code":3,"name":"roundRobin","support":true}],"httpMethodEnums":[{"code":null,"name":"get","support":true},{"code":null,"name":"post","support":true},{"code":null,"name":"put","support":true},{"code":null,"name":"delete","support":true}],"serializeEnums":[{"code":null,"name":"jdk","support":true},{"code":null,"name":"kryo","support":true},{"code":null,"name":"hessian","support":true},{"code":null,"name":"fastJson","support":true},{"code":null,"name":"protostuff","support":true}]}};
export default {
  getPlatform,
  userLogin
};
