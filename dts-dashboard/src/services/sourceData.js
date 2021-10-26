import request from "../utils/request";

self.CACHE_MEMORY = self.CACHE_MEMORY || {};
const baseUrl = document.getElementById("httpPath").innerHTML;


export async function addItem(params) {
  return request(`${baseUrl}/dts/sourceData/add`, {
    method: `POST`,
    body: {
      ...params
    }
  });
}

export async function deleteItem(params) {
  return request(`${baseUrl}/dts/sourceData/delete`, {
    method: `POST`,
    body: {
      ...params
    }
  });
}

export async function getItem(params) {
  let key = `sourceData_id_${params.sourceId}`;

  if (self.CACHE_MEMORY[key]) return Promise.resolve(self.CACHE_MEMORY[key]);
  return request(`${baseUrl}/dts/sourceData/get`, {
    method: `POST`,
    body: {
      ...params
    }
  }).then(resp => {
    if (resp.code === 200) {
      self.CACHE_MEMORY[key] = resp;
    }
    return resp;
  })
}

export async function listItems(params) {
  return request(`${baseUrl}/dts/sourceData/list`, {
    method: `POST`,
    body: {
      ...params
    }
  });
}

