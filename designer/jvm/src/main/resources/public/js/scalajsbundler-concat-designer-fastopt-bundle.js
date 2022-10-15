{
  var x45 = require("concat-with-sourcemaps");
  var x46 = require("fs");
  var x47 = new x45(true, "designer-fastopt-bundle.js", ";\n");
  x47.add("", x46.readFileSync("/Users/naden/Developer/harana/harana/designer/js/target/scala-2.13/scalajs-bundler/main/designer-fastopt-library.js", "utf-8"), x46.readFileSync("/Users/naden/Developer/harana/harana/designer/js/target/scala-2.13/scalajs-bundler/main/designer-fastopt-library.js.map"));
  x47.add("designer-fastopt-loader.js", x46.readFileSync("/Users/naden/Developer/harana/harana/designer/js/target/scala-2.13/scalajs-bundler/main/designer-fastopt-loader.js"));
  x47.add("", x46.readFileSync("/Users/naden/Developer/harana/harana/designer/js/target/scala-2.13/scalajs-bundler/main/designer-fastopt.js", "utf-8"), x46.readFileSync("/Users/naden/Developer/harana/harana/designer/js/target/scala-2.13/scalajs-bundler/main/designer-fastopt.js.map", "utf-8"));
  var x48 = Buffer.from("\n//# sourceMappingURL=designer-fastopt-bundle.js.map\n");
  var x49 = Buffer.concat([x47.content, x48]);
  x46.writeFileSync("/Users/naden/Developer/harana/harana/designer/js/target/scala-2.13/scalajs-bundler/main/designer-fastopt-bundle.js", x49);
  x46.writeFileSync("/Users/naden/Developer/harana/harana/designer/js/target/scala-2.13/scalajs-bundler/main/designer-fastopt-bundle.js.map", x47.sourceMap)
}