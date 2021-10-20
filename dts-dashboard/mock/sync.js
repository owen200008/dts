

export const listItems = (req, res) => {
  res.json({
    "code": 200,
    "message": "success",
    "data": {
      "pageSize": 1,
      "total": 150,
      "data": [
        {
          id: 2,
          "pipelineId": 22,
          "syncRuleType": "DEFAULT",
          "namespace": "ns1",
          "table": "tt1",
          "startGtid":"2xx"
        },
        {
          id: 2,
          "pipelineId": 22,
          "syncRuleType": "DEFAULT",
          "namespace": "ns1",
          "table": "tt1",
          "startGtid":"3xcv"
        }
      ],
    },
  });
};
export default {
  listItems,
};
