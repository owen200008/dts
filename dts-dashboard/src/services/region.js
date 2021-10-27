import request from "../utils/request";

const baseUrl = document.getElementById("httpPath").innerHTML;


export async function addItem(params) {
  return request(`${baseUrl}/dts/region/add`, {
    method: `POST`,
    body: {
      ...params
    }
  });
}

export async function updateItem(params) {
  return request(`${baseUrl}/dts/region/modify`, {
    method: `POST`,
    body: {
      ...params
    }
  });
}

export async function deleteItem(params) {
  return request(`${baseUrl}/dts/region/delete`, {
    method: `POST`,
    body: {
      ...params
    }
  });
}

export async function getItem(params) {
  return request(`${baseUrl}/dts/region/get`, {
    method: `POST`,
    body: {
      ...params
    }
  });
}

export async function listItems(params) {
  return request(`${baseUrl}/dts/region/list`, {
    method: `POST`,
    body: {
      ...params
    }
  });
}

export async function listMode(params) {
  return request(`${baseUrl}/dts/region/mode`, {
    method: `POST`,
    body: {
      ...params
    }
  });
}

