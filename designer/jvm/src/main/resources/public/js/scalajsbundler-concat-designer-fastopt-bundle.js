{
  var x65 = require("concat-with-sourcemaps");
  var x66 = require("fs");
  var x67 = new x65(true, "designer-fastopt-bundle.js", ";\n");
  x67.add("", x66.readFileSync("/Users/naden/Developer/harana/harana/designer/js/target/scala-2.13/scalajs-bundler/main/designer-fastopt-library.js", "utf-8"), x66.readFileSync("/Users/naden/Developer/harana/harana/designer/js/target/scala-2.13/scalajs-bundler/main/designer-fastopt-library.js.map"));
  x67.add("designer-fastopt-loader.js", x66.readFileSync("/Users/naden/Developer/harana/harana/designer/js/target/scala-2.13/scalajs-bundler/main/designer-fastopt-loader.js"));
  x67.add("", x66.readFileSync("/Users/naden/Developer/harana/harana/designer/js/target/scala-2.13/scalajs-bundler/main/designer-fastopt.js", "utf-8"), x66.readFileSync("/Users/naden/Developer/harana/harana/designer/js/target/scala-2.13/scalajs-bundler/main/designer-fastopt.js.map", "utf-8"));
  var x68 = Buffer.from("\n//# sourceMappingURL=designer-fastopt-bundle.js.map\n");
  var x69 = Buffer.concat([x67.content, x68]);
  x66.writeFileSync("/Users/naden/Developer/harana/harana/designer/js/target/scala-2.13/scalajs-bundler/main/designer-fastopt-bundle.js", x69);
  x66.writeFileSync("/Users/naden/Developer/harana/harana/designer/js/target/scala-2.13/scalajs-bundler/main/designer-fastopt-bundle.js.map", x67.sourceMap)
}