import request from "../utils/request";

const baseUrl = document.getElementById("httpPath").innerHTML;


export async function addItem(params) {
  return request(`${baseUrl}/dts/pair/add`, {
    method: `POST`,
    body: {
      ...params
    }
  });
}

export async function deleteItem(params) {
  return request(`${baseUrl}/dts/pair/delete`, {
    method: `POST`,
    body: {
      ...params
    }
  });
}

export async function getItem(params) {
  return request(`${baseUrl}/dts/pair/get`, {
    method: `POST`,
    body: {
      ...params
    }
  });
}

export async function listItems(params) {
  return request(`${baseUrl}/dts/pair/list`, {
    method: `POST`,
    body: {
      ...params
    }
  });
}

