

export const listItems = (req, res) => {
  res.json({
    "code": 200,
    "message": "success",
    "data": {
      "pageSize": 1,
      "total": 150,
      "data": [
        {
          "id": 122,
          "region": "机房1",
          "mode": "SELECT",
          "pipelineId": 22
        },
        {

          "id": 123,
          "region": "机房2",
          "mode": "SELECT",
          "pipelineId": 22
        },

      ],
    },
  });
};
export default {
  listItems,
};
