import React, { PureComponent } from "react";
import { Table, Input, Button, Popconfirm, Icon, Col, Row, Breadcrumb, Switch } from "antd";
import { connect } from "dva";
import { stringify } from 'qs';
import AddModal from "./AddModal";

@connect(({ task, loading }) => ({
  task,
  loading: loading.effects["task/fetch"] || loading.effects["task/switchItem"],
}))
export default class Task extends PureComponent {
  constructor(props) {
    super(props);

    this.state = {
      currentPage: 1,
      selectedRowKeys: [],
      name: '',
      popup: ""
    };
    this.pageSize = 10;
  }

  componentWillMount() {
    const { currentPage } = this.state;
    this.listItems(currentPage);
  }

  onSelectChange = selectedRowKeys => {
    this.setState({ selectedRowKeys });
  };

  listItems = page => {
    const { dispatch } = this.props;
    const { name } = this.state;
    dispatch({
      type: "task/fetch",
      payload: {
        name,
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
    const { currentPage, name } = this.state;

    this.setState({
      popup: (
        <AddModal
          disabled={true}
          {...item}
          handleOk={values => {
            dispatch({
              type: "task/update",
              payload: {
                ...values
              },
              fetchValue: {
                name,
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

  deleteClick = (task) => {
    const { dispatch } = this.props;
    const { name, currentPage } = this.state;
    dispatch({
      type: "task/delete",
      payload: {
        id: task.id
      },
      fetchValue: {
        name,
        pageNum: currentPage,
        pageSize: this.pageSize
      },
      callback: () => {
        this.setState({ selectedRowKeys: [] });
      }
    });
  };

  addClick = () => {
    this.setState({
      popup: (
        <AddModal
          disabled={false}
          handleOk={values => {
            const { dispatch } = this.props;
            dispatch({
              type: "task/add",
              payload: {
                ...values
              },
              callback: () => {
                this.closeModal();
                this.searchClick();
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





  onItemUpdate = (item, checked, e) => {
    let params = {
      id: item.id,
      valid: checked ? 1 : 0
    }

    const { dispatch } = this.props;
    dispatch({
      type: "task/updateItem",
      payload: params,
    });
    dispatch({
      type: "task/switchItem",
      payload: params,
      callback: () => {
        this.setState({ selectedRowKeys: [] });
      }
    });

  }

  toPipeline = (item) => {
    let { dispatch } = this.props;
    let { id } = item;
    dispatch({
      type: 'task/redirect',
      location: {
        pathname: '/system/pipeline',
        search: `?${stringify({
          id
        })}`
      }
    })
  };

  render() {

    const { task, loading } = this.props;
    const { dataList, total } = task;
    const { currentPage, name, popup } = this.state;

    const tableColumns = [
      {
        align: "center",
        title: "名称",
        dataIndex: "name",
        key: "name",
      },
      {
        align: "center",
        title: "状态",
        dataIndex: "valid",
        key: "valid",
        render: (text, record) => {
          let valid = record.valid;
          return (<Switch onChange={this.onItemUpdate.bind(this, record)} checked={valid} />)
        }
      },
      /* {
        align: "center",
        title: "编码",
        dataIndex: "encode",
        key: "encode",
      },
      {
        align: "center",
        title: "组类型",
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
        title: "mysql",
        dataIndex: "mysql",
        key: "mysql",
      }, */
      {
        align: "center",
        title: "操作",
        dataIndex: "time",
        key: "time",
        width: 120,
        render: (text, record) => {
          if (record.valid) return null;
          return (
            <div>
              <Icon
                title='编辑'
                type="edit"
                style={{ color: 'orange' }}
                onClick={() => {
                  this.editClick(record);
                }}
              />
              <Icon
                title='添加通道'
                type="retweet"
                style={{ marginLeft: 20, color: 'green' }}
                onClick={() => {
                  this.toPipeline(record);
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
                <Breadcrumb.Item>任务列表</Breadcrumb.Item>
              </Breadcrumb>
            </div>
            <div className="table-header" style={{ justifyContent: "normal" }}>
              <Input
                value={name}
                onChange={this.searchOnchange.bind(this, 'name')}
                placeholder="请输入名称"
                style={{ width: 240 }}
              />
              {/* <Input
                value={type}
                onChange={this.searchOnchange.bind(this, 'type')}
                placeholder="请输入数据类型"
                style={{ width: 240 }}
              /> */}
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
