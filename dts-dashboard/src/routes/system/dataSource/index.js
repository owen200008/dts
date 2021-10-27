import React, { PureComponent } from "react";
import { Table, Input, Button, Popconfirm, Icon, Col, Row, Breadcrumb, Select } from "antd";
import { connect } from "dva";
import AddModal from "./AddModal";

const { Option } = Select;
@connect(({ dataSource, loading }) => ({
  dataSource,
  loading: loading.effects["dataSource/fetch"],
}))
export default class DataSource extends PureComponent {
  constructor(props) {
    super(props);

    this.state = {
      currentPage: 1,
      selectedRowKeys: [],
      name: '',
      type: '',
      popup: ""
    };
    this.pageSize = 10;
  }

  componentWillMount() {
    const { dispatch } = this.props;
    const { currentPage } = this.state;
    dispatch({
      type: 'dataSource/fetchType'
    });
    this.listItems(currentPage);
  }

  onSelectChange = selectedRowKeys => {
    this.setState({ selectedRowKeys });
  };

  listItems = page => {
    const { dispatch } = this.props;
    const { name, type } = this.state;
    dispatch({
      type: "dataSource/fetch",
      payload: {
        name: name || undefined,
        type: type || undefined,
        pageNum: page,
        pageSize: this.pageSize
      }
    });
  };

  pageOnchange = page => {
    this.setState({ currentPage: page });
    this.listItems(page);
  };

  closeModal = () => {
    this.setState({ popup: "" });
  };

  editClick = item => {
    const { dispatch } = this.props;
    const { currentPage, name, type } = this.state;

    this.setState({
      popup: (
        <AddModal
          disabled={true}
          {...item}
          handleOk={values => {
            dispatch({
              type: "dataSource/update",
              payload: {
                ...values
              },
              fetchValue: {
                name: name || undefined,
                type: type || undefined,
                pageNum: currentPage,
                pageSize: this.pageSize
              },
              callback: () => {
                this.setState({ selectedRowKeys: [] });
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
  };

  searchOnchange = (key, e) => {
    const value = e.target.value;
    this.setState({ [key]: value });
  };

  searchClick = () => {
    this.listItems(1);
    this.setState({ currentPage: 1 });
  };

  deleteClick = (dataSource) => {
    const { dispatch } = this.props;
    const { name, type, currentPage } = this.state;
    dispatch({
      type: "dataSource/delete",
      payload: {
        id: dataSource.id
      },
      fetchValue: {
        name: name || undefined,
        type: type || undefined,
        pageNum: currentPage,
        pageSize: this.pageSize
      },
      callback: () => {
        this.setState({ selectedRowKeys: [] });
      }
    });
  };

  addClick = () => {
    const { currentPage, name, type } = this.state;
    this.setState({
      popup: (
        <AddModal
          disabled={false}
          handleOk={values => {
            const { dispatch } = this.props;
            dispatch({
              type: "dataSource/add",
              payload: {
                ...values
              },

              callback: () => {
                this.searchClick();
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
  };


  searchOnSelectchange = (key, value) => {
    this.setState({ [key]: value });
  };


  /* viewPluginConfig = (item) => {
    let { dispatch } = this.props;
    let { id } = item;
    dispatch({
      type: 'dataSource/redirect',
      location: {
        pathname: '/system/action',
        search: `?${stringify({
          id: id
        })}`
      }
    })
  }; */

  render() {

    const { dataSource, loading } = this.props;
    const { dataList, total, typeList } = dataSource;
    const { currentPage, name, type, popup } = this.state;

    const tableColumns = [
      {
        align: "center",
        title: "名称",
        dataIndex: "name",
        key: "name",
      },
      {
        align: "center",
        title: "类型",
        dataIndex: "type",
        key: "type",
      },
      {
        align: "center",
        title: "编码",
        dataIndex: "encode",
        key: "encode",
      },
      {
        align: "center",
        title: "slaveId",
        dataIndex: "slaveId",
        key: "slaveId",
      },
      {
        align: "center",
        title: "jdbcUrl",
        dataIndex: "url",
        key: "url",
      },
      {
        align: "center",
        title: "驱动",
        dataIndex: "driver",
        key: "driver",
      },
      {
        align: "center",
        title: "账号",
        dataIndex: "username",
        key: "username",
      },
      {
        align: "center",
        title: "密码",
        dataIndex: "password",
        key: "password",
      },
      {
        align: "center",
        title: "属性",
        dataIndex: "property",
        key: "property",
      },
      {
        align: "center",
        title: "操作",
        dataIndex: "time",
        key: "time",
        width: 120,
        render: (text, record) => {
          return (
            <div>
              <Icon
                title='查看'
                type="edit"
                style={{ color: 'orange' }}
                onClick={() => {
                  this.editClick(record);
                }}
              />
              <Popconfirm
                title="你确认删除吗"
                placement='bottom'
                onConfirm={() => {
                  this.deleteClick(record)
                }}
                okText="确认"
                cancelText="取消"
              >
                <Icon title='删除' type="delete" style={{ marginLeft: 20, color: 'red' }} />
              </Popconfirm>
              {/* <Icon
                title='查看配置'
                type="dash"
                style={{ marginLeft: 20, color: 'blue' }}
                onClick={this.viewPluginConfig.bind(this, record)}
              /> */}
            </div>
          );
        }
      }
    ];



    return (
      <div className="plug-content-wrap">

        <Row gutter={24}>
          <Col span={24}>
            <div className="table-header" style={{ paddingTop: 5 }}>
              <Breadcrumb>
                <Breadcrumb.Item><a href="#/home">Home</a></Breadcrumb.Item>
                <Breadcrumb.Item>数据源列表</Breadcrumb.Item>
              </Breadcrumb>
            </div>
            <div className="table-header" style={{ justifyContent: "normal" }}>
              <Select
                value={type || ''}
                style={{ width: 150 }}
                onChange={this.searchOnSelectchange.bind(this, 'type')}
                placeholder="请选择数据类型"
              >
                {typeList.map((item, index) => {
                  return (
                    <Option key={index} value={item}>
                      {item}
                    </Option>
                  );
                })}
              </Select>
              <Input
                value={name}
                onChange={this.searchOnchange.bind(this, 'name')}
                placeholder="请输入名称"
                style={{ width: 240 }}
              />

              <Button
                type="primary"
                style={{ marginLeft: 20, marginTop: 0 }}
                onClick={this.searchClick}
              >
                查询
              </Button>


              <Button
                style={{ marginLeft: 20, marginTop: 0 }}
                type="primary"
                onClick={this.addClick}
              >
                添加
              </Button>
            </div>
            <Table
              size="small"
              style={{ marginTop: 30 }}
              bordered
              loading={loading}
              columns={tableColumns}
              dataSource={dataList}
              pagination={{
                total,
                current: currentPage,
                pageSize: this.pageSize,
                onChange: this.pageOnchange
              }}
            />
            {popup}
          </Col>

        </Row>
      </div>
    )
      ;
  }
}
