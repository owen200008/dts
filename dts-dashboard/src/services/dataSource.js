import request from "../utils/request";

const baseUrl = document.getElementById("httpPath").innerHTML;


export async function addItem(params) {
  return request(`${baseUrl}/dts/entity/add`, {
    method: `POST`,
    body: {
      ...params
    }
  });
}

export async function deleteItem(params) {
  return request(`${baseUrl}/dts/entity/delete`, {
    method: `POST`,
    body: {
      ...params
    }
  });
}

export async function updateItem(params) {
  return request(`${baseUrl}/dts/entity/modify`, {
    method: `POST`,
    body: {
      ...params
    }
  });
}

export async function getItem(params) {
  return request(`${baseUrl}/dts/entity/get`, {
    method: `POST`,
    body: {
      ...params
    }
  });
}

export async function listItems(params) {
  return request(`${baseUrl}/dts/entity/list`, {
    method: `POST`,
    body: {
      ...params
    }
  });
}



export async function getProperty(params) {
  return request(`${baseUrl}/dts/entity/property`, {
    method: `POST`,
    body: {
      ...params
    }
  });
}

