import request from "../utils/request";

const baseUrl = document.getElementById("httpPath").innerHTML;

export async function addItem(params) {
  return request(`${baseUrl}/dts/targetData/add`, {
    method: `POST`,
    body: {
      ...params
    }
  });
}

export async function updateItem(params) {
  return request(`${baseUrl}/dts/targetData/modify`, {
    method: `POST`,
    body: {
      ...params
    }
  });
}

export async function deleteItem(params) {
  return request(`${baseUrl}/dts/targetData/delete`, {
    method: `POST`,
    body: {
      ...params
    }
  });
}

export async function getItem(params) {
  let key = `targetData_id_${params.targetId}`;
  let cacheValue = sessionStorage.getItem(key);
  if (cacheValue) return Promise.resolve(JSON.parse(cacheValue));
  return request(`${baseUrl}/dts/targetData/get`, {
    method: `POST`,
    body: {
      ...params
    }
  }).then(resp => {
    if (resp.code === 200) {
      sessionStorage.setItem(key, JSON.stringify(resp));
    }
    return resp;
  })
}

export async function listItems(params) {
  return request(`${baseUrl}/dts/targetData/list`, {
    method: `POST`,
    body: {
      ...params
    }
  });
}

