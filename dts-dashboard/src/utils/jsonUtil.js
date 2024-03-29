/* eslint-disable */
export function formatJSON(txt = '', compress = false/* 是否为压缩模式 */) {
  if (!txt) return txt;
  /* 格式化JSON源码(对象转换为JSON文本) */
  let indentChar = '    ';
  if (/^\s*$/.test(txt)) {
    // alert('数据为空,无法格式化! ');
    return txt;
  }
  let data;
  try {
    data = eval(`(${  txt  })`);
  } catch (e) {
    // alert('数据源语法错误,格式化失败! 错误信息: ' + e.description, 'err');
    return txt;
  }
  let draw = [], last = false, This = this, line = compress ? '' : '\n', nodeCount = 0, maxDepth = 0;

  let notify = function (name, value, isLast, indent/* 缩进 */, formObj) {
    nodeCount++;/* 节点计数 */
    let i = 0, tab = '';
    for (; i < indent; i++) tab += indentChar;/* 缩进HTML */
    tab = compress ? '' : tab;/* 压缩模式忽略缩进 */
    maxDepth = ++indent;/* 缩进递增并记录 */
    if (value && value.constructor == Array) {/* 处理数组 */
      draw.push(`${tab + (formObj ? (`"${  name  }":`) : '')  }[${  line}`);/* 缩进'[' 然后换行 */
      for (i = 0; i < value.length; i++)
        notify(i, value[i], i == value.length - 1, indent, false);
      draw.push(`${tab  }]${  isLast ? line : (`,${  line}`)}`);/* 缩进']'换行,若非尾元素则添加逗号 */
    } else if (value && typeof value === 'object') {/* 处理对象 */
      draw.push(`${tab + (formObj ? (`"${  name  }":`) : '')  }{${  line}`);/* 缩进'{' 然后换行 */
      let len = 0;
      i = 0;
      for (let key in value) len++;
      for (let key in value) notify(key, value[key], ++i == len, indent, true);
      draw.push(`${tab  }}${  isLast ? line : (`,${  line}`)}`);/* 缩进'}'换行,若非尾元素则添加逗号 */
    } else {
      if (typeof value === 'string') value = JSON.stringify(value);
      draw.push(tab + (formObj ? (`"${  name  }":`) : '') + value + (isLast ? '' : ',') + line);
    }
  };
  let isLast = true, indent = 0;
  notify('', data, isLast, indent, false);
  return draw.join('');
}

export default {
  formatJSON
}
