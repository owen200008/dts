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

export async function listItemsById(params) {
  return request(`${baseUrl}/dts/region/get/pipelineId`, {
    method: `POST`,
    body: {
      ...params
    }
  });
}

