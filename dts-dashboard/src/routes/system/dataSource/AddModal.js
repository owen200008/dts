import React, { PureComponent, Fragment } from "react";
import { Modal, Form, Switch, Input, Select, Divider } from "antd";
import { connect } from "dva";
import ace from 'ace-builds'
import 'ace-builds/webpack-resolver'
import 'ace-builds/src-noconflict/theme-xcode' // 默认设置的主题
import 'ace-builds/src-noconflict/mode-json' // 默认设置的语言模式
import { formatJSON } from "../../../utils/jsonUtil";

const { Option } = Select;
const FormItem = Form.Item;

@connect(({ global, dataSource }) => ({
  platform: global.platform,
  typeList: dataSource.typeList
}))
class AddModal extends PureComponent {
  constructor(props) {
    super(props);
    const { id, property, type } = props;
    this.cache = id ? {
      [type]: property?.trim()
    } : {};
  }

  componentDidMount() {
    setTimeout(this.initAceEditor, 100);
  }

  handleSubmit = e => {
    const { form, handleOk, id = "" } = this.props;
    e.preventDefault();
    form.validateFieldsAndScroll((err, values) => {
      if (!err) {

        let { name, type, encode, /* slaveId, */ url, driver, username, password } = values;
        let property = this.jsonEncode(this.configEditor);
        try {
          property && JSON.parse(property);
        } catch (ex) {
          return alert('请输入正确配置');
        }

        handleOk({ id, name, type, encode, /* slaveId, */ url, driver, username, password, property });
      }
    });
  };

  initAceEditor = () => {
    this.destroyEditor();
    this.editor = ace.edit(this.refs.editor);
    this.editor.setTheme('ace/theme/xcode');
    let JsMode = ace.require('ace/mode/json').Mode;
    this.editor.session.setMode(new JsMode());
  }

  destroyEditor = () => {
    if (this.editor) {
      this.editor.destroy();
      this.editor.container.remove();
      this.editor = null;
    }
  }

  jsonEncode = () => {
    let jsonDoc = this.editor.getValue();
    jsonDoc = formatJSON(jsonDoc, true);
    return jsonDoc;
  }

  onTypeChange = (type) => {
    if (!type) return;
    const promise = this.cache[type] ? Promise.resolve(this.cache[type]) : new Promise(resove => {
      const { dispatch } = this.props;
      dispatch({
        type: 'dataSource/fetchProperty',
        payload: {
          type
        },
        callback: (data) => {
          let str = JSON.stringify(data);
          this.cache[type] = str;
          resove(str);
        }
      })
    });
    promise.then(str => {
      this.editor.setValue(formatJSON(str));
    })
  }

  render() {
    let { typeList, handleCancel, form, name, type, encode, /* slaveId, */ url, driver, username, password, property } = this.props;



    const { getFieldDecorator } = form;

    const formItemLayout = {
      labelCol: {
        sm: { span: 5 }
      },
      wrapperCol: {
        sm: { span: 19 }
      }
    };





    return (
      <Modal
        width={520}
        centered
        title="数据源"
        visible
        okText="确定"
        cancelText="取消"
        onOk={this.handleSubmit}
        onCancel={handleCancel}
        maskClosable={false}
      >
        <Form onSubmit={this.handleSubmit} className="login-form">
          <FormItem label="名称" {...formItemLayout}>
            {getFieldDecorator("name", {
              rules: [{ required: true, message: "请输入数据源名称" }],
              initialValue: name,
            })(
              <Input placeholder="请输入数据源名称" />
            )}
          </FormItem>
          <FormItem label="数据类型" {...formItemLayout}>
            {getFieldDecorator("type", {
              rules: [{ required: true, message: "请选择数据类型" }],
              initialValue: type,
            })(
              <Select onChange={this.onTypeChange}>
                {typeList.map((item, index) => {
                  return (
                    <Option key={index} value={item}>
                      {item}
                    </Option>
                  );
                })}
              </Select>
            )}
          </FormItem>
          <FormItem label="数据编码格式" {...formItemLayout}>
            {getFieldDecorator("encode", {
              rules: [{ required: false, message: "请输入数据编码格式" }],
              initialValue: encode,
            })(
              <Input placeholder="请输入数据编码格式" />
            )}
          </FormItem>
          {/* <FormItem label="slaveId" {...formItemLayout}>
            {getFieldDecorator("slaveId", {
              rules: [{ required: true, message: "请输入slaveId" }],
              initialValue: slaveId,
            })(
              <Input placeholder="请输入slaveId" />
            )}
            </FormItem> */}
          <FormItem label="JDBC URL" {...formItemLayout}>
            {getFieldDecorator("url", {
              rules: [{ required: true, message: "请输入jdbc url" }],
              initialValue: url,
            })(
              <Input placeholder="请输入jdbc url" />
            )}
          </FormItem>
          <FormItem label="驱动" {...formItemLayout}>
            {getFieldDecorator("driver", {
              rules: [{ required: true, message: "请输入驱动" }],
              initialValue: driver,
            })(
              <Input placeholder="请输入驱动" />
            )}
          </FormItem>
          <FormItem label="账号" {...formItemLayout}>
            {getFieldDecorator("username", {
              rules: [{ required: true, message: "请输入账号" }],
              initialValue: username,
            })(
              <Input placeholder="请输入账号" />
            )}
          </FormItem>
          <FormItem label="密码" {...formItemLayout}>
            {getFieldDecorator("password", {
              rules: [{ required: true, message: "请输入密码" }],
              initialValue: password,
            })(
              <Input placeholder="请输入密码" />
            )}
          </FormItem>
          <FormItem label="属性" {...formItemLayout}>
            <div style={{ width: '100%', height: '200px' }} ref="editor">{formatJSON(property)}</div>
          </FormItem>
          {/* <FormItem label="类型" {...formItemLayout}>
            {getFieldDecorator("typePlugin", {
              rules: [{required: true, message: "请选择类型"}],
              initialValue: typePlugin// pluginTypeEnums[typePlugin].name
            })(
              <Select>
                {pluginTypeEnums.map((item, index) => {
                  return (
                    <Option key={index} value={item.code}>
                      {item.name}
                    </Option>
                  );
                })}
              </Select>
            )}
          </FormItem>
          <FormItem {...formItemLayout} label="是否开启">
            {getFieldDecorator("isValid", {
              initialValue: isValid === 1,
              valuePropName: "checked"
            })(<Switch/>)}
          </FormItem> */}
        </Form>
      </Modal>
    );
  }
}

export default Form.create()(AddModal);
