// use localStorage to store the authority info, which might be sent from server in actual project.
export function getAuthority() {
  // return localStorage.getItem('antd-pro-authority') || ['admin', 'user'];
  // return sessionStorage.getItem('antd-pro-authority') || '';
  return localStorage.getItem('antd-pro-authority') || '';
}

export function setAuthority(authority) {
  // return sessionStorage.setItem('antd-pro-authority', authority);
  return localStorage.setItem('antd-pro-authority', authority);
}
