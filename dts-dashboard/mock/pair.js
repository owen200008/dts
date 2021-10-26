

export const listItems = (req, res) => {
  res.json({
    "code": 200,
    "message": "success",
    "data": {
      "pageSize": 1,
      "total": 150,
      "data": [
        {
          "id": 1,
          "pipelineId": 22,
          "sourceDatamediaId": 33,
          "targetDatamediaId": 44
        },
        {
          "id": 2,
          "pipelineId": 22,
          "sourceDatamediaId": 33,
          "targetDatamediaId": 44
        }
      ],
    },
  });
};

export const listItemsById = (req, res) => {
  res.json({
    "code": 200,
    "msg": "",
    "data":
      [
        {
          "id": 111,
          "pipelineId": 22,
          "sourceDatamediaId": 33,
          "targetDatamediaId": 44
        },
        {
          "id": 111,
          "pipelineId": 22,
          "sourceDatamediaId": 33,
          "targetDatamediaId": 44
        }

      ]

  });
};

export default {
  listItems,
  listItemsById
};
