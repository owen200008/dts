

export const listItems = (req, res) => {
  res.json({
    "code": '200',
    "message": "success",
    "data": {
      "pageSize": 1,
      "totalCount": 150,
      "entityList": [
        {
          "id": 2,
          "name": "123任务abc",
          "valid": 1
        },
        {
          "id": 3,
          "name": "456任务efg",
          "valid": 0
        },


      ],
    },
  });
};
export default {
  listItems,
};
