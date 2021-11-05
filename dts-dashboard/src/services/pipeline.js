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

  let cacheValue = sessionStorage.getItem(key);
  if (cacheValue) return Promise.resolve(JSON.parse(cacheValue));
  return request(`${baseUrl}/dts/pipeline/get`, {
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
  return request(`${baseUrl}/dts/pipeline/list`, {
    method: `POST`,
    body: {
      ...params
    }
  });
}

export async function dispatchRule(params) {
  return request(`${baseUrl}/dts/pipeline/dispatchRule`, {
    method: `POST`,
    body: {
      ...params
    }
  });
}

export async function defaultParams(params) {
  return request(`${baseUrl}/dts/pipeline/defaultParams`, {
    method: `POST`,
    body: {
      ...params
    }
  });
}


