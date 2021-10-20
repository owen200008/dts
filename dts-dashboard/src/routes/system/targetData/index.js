import React, { PureComponent } from "react";
import { Table, Input, Button, Popconfirm, Icon, Col, Row, Breadcrumb, Switch } from "antd";
import { connect } from "dva";

import AddModal from "../sourceData/AddModal";

@connect(({ targetData, loading }) => ({
  targetData,
  loading: loading.effects["targetData/fetch"],
}))
export default class TargetData extends PureComponent {
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
      type: "targetData/fetch",
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
          title="目标数据"
          disabled={true}
          {...item}
          handleOk={values => {
            dispatch({
              type: "targetData/update",
              payload: {
                ...values
              },
              fetchValue: {
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

  deleteClick = (targetData) => {
    const { dispatch } = this.props;
    const { name, currentPage } = this.state;
    dispatch({
      type: "targetData/delete",
      payload: {
        id: targetData.id
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
    const { currentPage, name } = this.state;
    this.setState({
      popup: (
        <AddModal
          title="目标数据"
          disabled={false}
          handleOk={values => {
            const { dispatch } = this.props;
            dispatch({
              type: "targetData/add",
              payload: {
                ...values
              },
              fetchValue: {
                name,
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

    const { targetData, loading } = this.props;
    const { dataList, total } = targetData;
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
        title: "数据库名",
        dataIndex: "namespace",
        key: "namespace",
      },
      {
        align: "center",
        title: "数据表名",
        dataIndex: "table",
        key: "table",
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
                <Breadcrumb.Item>目标数据列表</Breadcrumb.Item>
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
