import { stringify } from "qs";
import request from "../utils/request";

const baseUrl = document.getElementById("httpPath").innerHTML;

/* 添加用户 */
export async function addUser(params) {
  return request(`${baseUrl}/dashboardUser`, {
    method: `POST`,
    body: {
      ...params
    }
  });
}

/* 删除用户 */
export async function deleteUser(params) {
  return request(`${baseUrl}/dashboardUser/batch`, {
    method: `POST`,
    body: [...params.list]
  });
}

/* 修改用户 */
export async function updateUser(params) {
  return request(`${baseUrl}/dashboardUser/${params.id}`, {
    method: `PUT`,
    body: {
      userName: params.userName,
      password: params.password,
      role: params.role,
      enabled: params.enabled
    }
  });
}

/* 查询所有用户 */
export async function getAllUsers(params) {
  const { userName, currentPage, pageSize } = params;
  let myParams = params;
  if (userName) {
    myParams = params;
  } else {
    myParams = { currentPage, pageSize };
  }
  return request(`${baseUrl}/dashboardUser?${stringify(myParams)}`, {
    method: `GET`
  });
}

/* 查询单个用户 */
export async function findUser(params) {
  return request(`${baseUrl}/dashboardUser/${params.id}`, {
    method: `GET`
  });
}


/* 登录 */
export async function queryLogin(params) {
  // ?${stringify(params)}
  return request(`${baseUrl}/dts/login`, {
    method: `POST`,
    body: {
      ...params
    }
  });
}