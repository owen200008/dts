import request from "../utils/request";

const baseUrl = document.getElementById("httpPath").innerHTML;
self.CACHE_MEMORY = self.CACHE_MEMORY || {};

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

export async function updateItem(params) {
  return request(`${baseUrl}/dts/pipeline/modify`, {
    method: `POST`,
    body: {
      ...params
    }
  });
}

export async function getItem(params) {
  let key = `pipeline_id_${params.pipelineId}`;

  if (self.CACHE_MEMORY[key]) return Promise.resolve(self.CACHE_MEMORY[key]);
  return request(`${baseUrl}/dts/pipeline/get`, {
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
  return request(`${baseUrl}/dts/pipeline/list`, {
    method: `POST`,
    body: {
      ...params
    }
  });
}


