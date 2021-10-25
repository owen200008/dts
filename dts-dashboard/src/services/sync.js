import request from "../utils/request";

const baseUrl = document.getElementById("httpPath").innerHTML;


export async function addItem(params) {
  return request(`${baseUrl}/dts/syncRule/add`, {
    method: `POST`,
    body: {
      ...params
    }
  });
}

export async function deleteItem(params) {
  return request(`${baseUrl}/dts/syncRule/delete`, {
    method: `POST`,
    body: {
      ...params
    }
  });
}

export async function getItem(params) {
  return request(`${baseUrl}/dts/syncRule/get`, {
    method: `POST`,
    body: {
      ...params
    }
  });
}

export async function listItems(params) {
  return request(`${baseUrl}/dts/syncRule/list`, {
    method: `POST`,
    body: {
      ...params
    }
  });
}

export async function listItemsById(params) {
  return request(`${baseUrl}/dts/syncRule/get/pipelineId`, {
    method: `POST`,
    body: {
      ...params
    }
  });
}