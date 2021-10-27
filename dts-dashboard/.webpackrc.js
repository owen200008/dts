const path = require("path");

export default {
  entry: "./src/index.js",
  extraBabelPlugins: [
    ["import", { libraryName: "antd", libraryDirectory: "es", style: true }]
  ],
  env: {
    development: {
      extraBabelPlugins: ["dva-hmr"]
    }
  },
  outputPath: path.resolve(__dirname, "../dts-admin/src/main/resources/static"),
  alias: {
    components: path.resolve(__dirname, "src/components/")
  },
  ignoreMomentLocale: true,
  theme: "./src/theme.js",
  html: {
    template: "./src/index.ejs"
  },
  lessLoaderOptions: {
    javascriptEnabled: true
  },
  disableDynamicImport: true,
  publicPath: "/",
  hash: true,
  proxy: {
    "/": {
      //target:"http://172.28.9.2:8112",
      target:"https://dts-admin.blurams.vip",
      changeOrigin: true,
      pathRewrite: { "^/": "" }
    }
  }
};
