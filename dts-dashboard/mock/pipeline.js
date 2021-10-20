export const getItem = (req, res) => {
  res.json({
    "code": '200',
    "message": "success",
    "data": {
      "id": 2,
      "name": "通道1",
      "pipelineParams": "",
      "sourceEntityId": 11111,
      "targetEntityId": 12222,
    }
  });
};

export const listItems = (req, res) => {
  res.json({
    "code": 200,
    "message": "success",
    "data": {
      "pageSize": 1,
      "total": 150,
      "data": [
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
  getItem
};
