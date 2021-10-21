import request from "../utils/request";

const baseUrl = document.getElementById("httpPath").innerHTML;


export async function addItem(params) {
  return request(`${baseUrl}/dts/sync/add`, {
    method: `POST`,
    body: {
      ...params
    }
  });
}

export async function deleteItem(params) {
  return request(`${baseUrl}/dts/sync/delete`, {
    method: `POST`,
    body: {
      ...params
    }
  });
}

export async function getItem(params) {
  return request(`${baseUrl}/dts/sync/get`, {
    method: `POST`,
    body: {
      ...params
    }
  });
}

export async function listItems(params) {
  return request(`${baseUrl}/dts/sync/list`, {
    method: `POST`,
    body: {
      ...params
    }
  });
}

export async function listItemsById(params) {
  return request(`${baseUrl}/dts/sync/get/pipelineId`, {
    method: `POST`,
    body: {
      ...params
    }
  });
}