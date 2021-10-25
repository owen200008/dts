

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
          "name": "123任务abc",
          "valid": true
        },
        {
          "id": 3,
          "name": "456任务efg",
          "valid": false
        },


      ],
    },
  });
};
export default {
  listItems,
};
