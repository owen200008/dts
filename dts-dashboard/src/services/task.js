import request from "../utils/request";

const baseUrl = document.getElementById("httpPath").innerHTML;


export async function addItem(params) {
  return request(`${baseUrl}/dts/task/add`, {
    method: `POST`,
    body: {
      ...params
    }
  });
}

export async function deleteItem(params) {
  return request(`${baseUrl}/dts/task/delete`, {
    method: `POST`,
    body: {
      ...params
    }
  });
}

export async function updateItem(params) {
  return request(`${baseUrl}/dts/task/modify`, {
    method: `POST`,
    body: {
      ...params
    }
  });
}

export async function switchItem(params) {
  return request(`${baseUrl}/dts/task/switch`, {
    method: `POST`,
    body: {
      ...params
    }
  });
}

export async function listItems(params) {
  return request(`${baseUrl}/dts/task/list`, {
    method: `POST`,
    body: {
      ...params
    }
  });
}
