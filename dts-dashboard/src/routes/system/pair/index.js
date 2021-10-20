import React, { PureComponent } from "react";
import { Table, Input, Button, Popconfirm, Icon, Col, Row, Breadcrumb, Switch } from "antd";
import { connect } from "dva";
import AddModal from "./AddModal";

@connect(({ pair, loading }) => ({
  pair,
  loading: loading.effects["pair/fetch"],
}))
export default class Pair extends PureComponent {
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
    const { currentPage } = this.state;
    this.listItems(currentPage);
  }

  onSelectChange = selectedRowKeys => {
    this.setState({ selectedRowKeys });
  };

  listItems = page => {
    const { dispatch } = this.props;
    const { name, type } = this.state;
    dispatch({
      type: "pair/fetch",
      payload: {
        name,
        type,
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
              type: "pair/update",
              payload: {
                ...values
              },
              fetchValue: {
                type,
                name,
                pageNum: currentPage,
                pageSize: 12
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

  deleteClick = (pair) => {
    const { dispatch } = this.props;
    const { name, type, currentPage } = this.state;
    dispatch({
      type: "pair/delete",
      payload: {
        id: pair.id
      },
      fetchValue: {
        name, type,
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
              type: "pair/add",
              payload: {
                ...values
              },
              fetchValue: {
                name, type,
                pageNum: currentPage,
                pageSize: this.pageSize
              },
              callback: () => {
                this.closeModal();
                /* dispatch({
                  type: "global/fetchPlugins",
                  payload: {
                    callback: () => {
                    }
                  }
                }); */
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






  render() {

    const { pair, loading } = this.props;
    const { dataList, total } = pair;
    const { currentPage, name, popup } = this.state;

    const tableColumns = [
      {
        align: "center",
        title: "通道名称",
        dataIndex: "pipelineName",
        key: "pipelineName",
      },
      {
        align: "center",
        title: "源数据",
        dataIndex: "sourceData",
        key: "sourceData",
      },
      {
        align: "center",
        title: "目标数据",
        dataIndex: "targetData",
        key: "targetData",
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

              <Popconfirm
                title="你确认删除吗"
                placement='bottom'
                onConfirm={() => {
                  this.deleteClick(record)
                }}
                okText="确认"
                cancelText="取消"
              >
                <Icon title='删除' type="delete" style={{ marginLeft: 0, color: 'red' }} />
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
    );
  }
}
