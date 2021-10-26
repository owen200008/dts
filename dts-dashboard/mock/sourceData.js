

export const getItem = (req, res) => {
  res.json({
    "code": 200,
    "message": "success",
    "data": {
      "id": 11,
      "name": "11",
      "namespace": "xx",
      "table": "xx"
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
          "id": 11,
          "name": "11",
          "namespace": "xx",
          "table": "xx"
        },
        {
          "id": 22,
          "name": "22",
          "namespace": "xx",
          "table": "xx"
        },

      ],
    },
  });
};
export default {
  listItems,
  getItem
};
