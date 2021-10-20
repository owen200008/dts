import request from "../utils/request";

const baseUrl = document.getElementById("httpPath").innerHTML;


export async function addItem(params) {
  return request(`${baseUrl}/dts/pipeline/add`, {
    method: `POST`,
    body: {
      ...params
    }
  });
}

export async function deleteItem(params) {
  return request(`${baseUrl}/dts/pipeline/delete`, {
    method: `POST`,
    body: {
      ...params
    }
  });
}

export async function getItem(params) {
  return request(`${baseUrl}/dts/pipeline/get`, {
    method: `POST`,
    body: {
      ...params
    }
  });
}

export async function listItems(params) {
  return request(`${baseUrl}/dts/pipeline/list`, {
    method: `POST`,
    body: {
      ...params
    }
  });
}

export async function pairAddItem(params) {
  return request(`${baseUrl}/dts/pipeline/pair/add`, {
    method: `POST`,
    body: {
      ...params
    }
  });
}

export async function regionAddItem(params) {
  return request(`${baseUrl}/dts/pipeline/region/add`, {
    method: `POST`,
    body: {
      ...params
    }
  });
}

export async function regionGetItem(params) {
  return request(`${baseUrl}/dts/region/get/pipeline`, {
    method: `POST`,
    body: {
      ...params
    }
  });
}

export async function entityGetItem(params) {
  return request(`${baseUrl}/dts/entity/get/pipeline`, {
    method: `POST`,
    body: {
      ...params
    }
  });
}