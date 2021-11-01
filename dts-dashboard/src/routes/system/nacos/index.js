import React, { PureComponent } from "react";
import { Table, Input, Button, Popconfirm, Icon, Col, Row, Breadcrumb, Select } from "antd";
import { connect } from "dva";
import { parse } from "qs";

const { Option } = Select;

@connect(({ region, loading }) => ({
  dataList: region.nacosList,
  regionList: region.dataList,
  loading: loading.effects["nacos/fetchNacos"],
}))
export default class TargetData extends PureComponent {
  constructor(props) {
    super(props);
    let { regionId = 0 } = parse(props.location.search.substring(1)) || {};

    this.state = {
      currentPage: 1,
      regionId: +regionId,
    };

    this.pageSize = 10;
  }

  componentWillMount() {
    const { dispatch } = this.props;
    const { currentPage } = this.state;
    dispatch({
      type: "region/fetch",
      payload: {
        pageNum: 1,
        pageSize: 10000
      },
      callback: () => {
        this.listItems(currentPage);
      }
    })


  }



  listItems = page => {
    const { dispatch, regionList } = this.props;
    const { regionId, } = this.state;
    dispatch({
      type: "region/fetchNacos",
      payload: {
        region: regionList.find(item => item.id === regionId)?.region || undefined,
        pageNum: page,
        pageSize: this.pageSize
      }
    });
  };




  searchClick = () => {
    this.listItems(1);
    this.setState({ currentPage: 1 });
  };

  searchOnSelectchange = (key, value) => {
    this.setState({ [key]: value });
  };

  render() {

    const { dataList, loading, regionList } = this.props;
    const { regionId } = this.state;

    const tableColumns = [
      {
        align: "center",
        title: "Region",
        dataIndex: "region",
        key: "region",
      },
      {
        align: "center",
        title: "IP",
        dataIndex: "ip",
        key: "ip",
      },
      {
        align: "center",
        title: "端口",
        dataIndex: "port",
        key: "port",
      },
      {
        align: "center",
        title: "权重",
        dataIndex: "weight",
        key: "weight",
      },
      {
        align: "center",
        title: "metaData",
        dataIndex: "metaData",
        key: "metaData",
        render: (text, record) => {
          return JSON.stringify(text);
        }
      },

    ];



    return (
      <div className="plug-content-wrap">

        <Row gutter={24}>
          <Col span={24}>
            <div className="table-header" style={{ paddingTop: 5 }}>
              <Breadcrumb>
                <Breadcrumb.Item><a href="#/home">Home</a></Breadcrumb.Item>
                <Breadcrumb.Item>Region实例列表</Breadcrumb.Item>
              </Breadcrumb>
            </div>
            <div className="table-header" style={{ justifyContent: "normal" }}>
              <Select
                allowClear
                value={regionId || ''}
                style={{ width: 150 }}
                onChange={this.searchOnSelectchange.bind(this, 'regionId')}
                placeholder="请选择Region"
              >
                {regionList.map((item, index) => {
                  return (
                    <Option key={index} value={item.id}>
                      {item.region}
                    </Option>
                  );
                })}
              </Select>
              <Button
                type="primary"
                style={{ marginLeft: 20, marginTop: 0 }}
                onClick={this.searchClick}
              >
                查询
              </Button>


            </div>
            <Table
              size="small"
              style={{ marginTop: 30 }}
              bordered
              loading={loading}
              columns={tableColumns}
              dataSource={dataList}
              pagination={false}
            />
          </Col>

        </Row>
      </div>
    )
      ;
  }
}
