import {stringify} from "qs";
import request from "../utils/request";

const baseUrl = document.getElementById("httpPath").innerHTML;


export async function addAction(params) {
  return request(`${baseUrl}/api/v1/actions`, {
    method: `POST`,
    body: {
      ...params
    }
  });
}

/* 删除插件 */
export async function deleteAction(params) {
  return request(`${baseUrl}/api/v1/actions/${params.id}`, {
    method: `DELETE`,

  });
}

/* 修改插件 */
export async function updateAction(params) {
  return request(`${baseUrl}/api/v1/actions`, {
    method: `PUT`,
    body: params
  });
}

/* 查询所有插件 */
export async function getAllActions(params) {  
  return request(`${baseUrl}/api/v1/actions?${stringify(params)}`, {
    method: `GET`
  });
}

