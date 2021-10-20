export const regionItems = (req, res) => {
  res.json({
    "code": "200",
    "msg": "success",
    "data":
      [
        {
          "id": 111,
          "region": "zq-cloud",
          "mode": "SELECT"
        },
        {
          "id": 111,
          "region": "zq-cloud2",
          "mode": "LOAD"
        }
      ]
  })
}

export const regionItems = (req, res) => {
  res.json({
    "code": "200",
    "msg": "success",
    "data":
      [
        {
          "id": 111,
          "region": "zq-cloud",
          "mode": "SELECT"
        },
        {
          "id": 111,
          "region": "zq-cloud2",
          "mode": "LOAD"
        }
      ]
  })
}

export const listItems = (req, res) => {
  res.json({
    "code": 200,
    "message": "success",
    "data": {
      "pageSize": 1,
      "totalCount": 150,
      "entityList": [
        {
          "id": 2,
          "name": "通道1",
          "pipelineParams": "",
          "sourceEntityId": 11111,
          "targetEntityId": 12222,
        },
        {
          "id": 3,
          "name": "通道2",
          "pipelineParams": "",
          "sourceEntityId": 11111,
          "targetEntityId": 12222,
        },

      ],
    },
  });
};
export default {
  listItems,
  regionItems
};
