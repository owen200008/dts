import React, { PureComponent, Fragment } from "react";
import { Modal, Form, Switch, Input, Select, Button } from "antd";
import { connect } from "dva";
import AddRegionModal from "../region/AddModal";

const { Option } = Select;
const FormItem = Form.Item;

@connect(({ pipeline, region }) => ({
  pipelineList: pipeline.dataList,
  modeList: region.modeList,
  regionList: region.dataList
}))
class AddModal extends PureComponent {
  constructor(props) {
    super(props);
    const { dispatch, regionId } = props;
    this.state = {
      popup: null,
      regionId
    }
    dispatch({
      type: 'region/fetchMode'
    })
    dispatch({
      type: 'region/fetch',
      payload: {
        pageNum: 1,
        pageSize: 10000
      }
    })
  }

  handleSubmit = e => {
    let { form, handleOk, id } = this.props;
    e.preventDefault();
    form.validateFieldsAndScroll((err, values) => {
      if (!err) {

        let { regionId, mode, pipelineId } = values;

        handleOk({
          id, pipelineId, regionId, mode
        });
      }
    });
  };

  filterOption = (input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0

  closeModal = () => {
    this.setState({ popup: null });
  };

  addRegion = (key) => {
    const { dispatch } = this.props;
    this.setState({
      popup: (
        <AddRegionModal
          disabled={false}
          handleOk={values => {
            dispatch({
              type: "region/add",
              payload: {
                ...values
              },
              fetchValue: {
                pageNum: 1,
                pageSize: 10000
              },
              callback: ({ regionId }) => {
                this.setState({
                  [key]: regionId
                })
                this.closeModal();
              }
            });
          }}
          handleCancel={() => {
            this.closeModal();
          }}
        />
      )
    });
  }

  render() {
    let { regionList, modeList, pipelineList, handleCancel, form, mode, pipelineId } = this.props;

    let { regionId, popup } = this.state;

    const { getFieldDecorator } = form;

    const formItemLayout = {
      labelCol: {
        sm: { span: 6 }
      },
      wrapperCol: {
        sm: { span: 18 }
      }
    };





    return (
      <Modal
        width={520}
        centered
        title="Region"
        visible
        okText="确定"
        cancelText="取消"
        onOk={this.handleSubmit}
        onCancel={handleCancel}
        maskClosable={false}
      >
        <Form onSubmit={this.handleSubmit} className="login-form">
          <FormItem label="通道" {...formItemLayout}>
            {getFieldDecorator("pipelineId", {
              rules: [{ required: true, message: "请选择通道" }],
              initialValue: pipelineId || ''
            })(
              <Select allowClear={true} showSearch={true} filterOption={this.filterOption}>
                {pipelineList.map((item, index) => {
                  return (
                    <Option key={index} value={item.id}>
                      {item.name}
                    </Option>
                  );
                })}
              </Select>
            )}
          </FormItem>
          <FormItem label="Region" {...formItemLayout}>
            {getFieldDecorator("regionId", {
              rules: [{ required: true, message: "请选择Region" }],
              initialValue: regionId || ''
            })(
              <Select style={{ width: '70%' }} allowClear={true} showSearch={true} filterOption={this.filterOption}>
                {regionList.map((item, index) => {
                  return (
                    <Option key={index} value={item.id}>
                      {item.region}
                    </Option>
                  );
                })}
              </Select>
            )}
            <Button
              style={{ marginLeft: 20, marginTop: 0 }}
              onClick={this.addRegion.bind(this, 'regionId')}
              icon="plus"
            >
              添加
            </Button>
          </FormItem>
          <FormItem label="类型" {...formItemLayout}>
            {getFieldDecorator("mode", {
              rules: [{ required: true, message: "请选择类型" }],
              initialValue: mode
            })(
              <Select>
                {modeList.map((item, index) => {
                  return (
                    <Option key={index} value={item}>
                      {item}
                    </Option>
                  );
                })}
              </Select>
            )}
          </FormItem>


          {/* <FormItem label="组类型	" {...formItemLayout}>
            {getFieldDecorator("slaveId", {
              rules: [{ required: true, message: "请输入组类型" }],
              initialValue: slaveId,
            })(
              <Input placeholder="请输入组类型" disabled={disable}  />
            )}
          </FormItem>
          <FormItem label="JDBC URL" {...formItemLayout}>
            {getFieldDecorator("url", {
              rules: [{ required: true, message: "请输入jdbc url" }],
              initialValue: url,
            })(
              <Input placeholder="请输入jdbc url" disabled={disable}  />
            )}
          </FormItem>
          <FormItem label="驱动" {...formItemLayout}>
            {getFieldDecorator("driver", {
              rules: [{ required: true, message: "请输入驱动" }],
              initialValue: driver,
            })(
              <Input placeholder="请输入驱动" disabled={disable}  />
            )}
          </FormItem>
          <FormItem label="账号" {...formItemLayout}>
            {getFieldDecorator("username", {
              rules: [{ required: true, message: "请输入账号" }],
              initialValue: username,
            })(
              <Input placeholder="请输入账号" disabled={disable}  />
            )}
          </FormItem>
          <FormItem label="密码" {...formItemLayout}>
            {getFieldDecorator("password", {
              rules: [{ required: true, message: "请输入密码" }],
              initialValue: password,
            })(
              <Input placeholder="请输入密码" disabled={disable}  />
            )}
          </FormItem>
          <FormItem label="mysql" {...formItemLayout}>
            {getFieldDecorator("mysql", {
              rules: [{ required: false, message: "请输入mysql" }],
              initialValue: mysql,
            })(
              <Input placeholder="请输入mysql" disabled={disable}  />
            )}
          </FormItem>
           <FormItem label="类型" {...formItemLayout}>
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
        {popup}
      </Modal>
    );
  }
}

export default Form.create()(AddModal);
