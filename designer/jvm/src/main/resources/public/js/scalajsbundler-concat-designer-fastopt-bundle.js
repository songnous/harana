{
  var x495 = require("concat-with-sourcemaps");
  var x496 = require("fs");
  var x497 = new x495(true, "designer-fastopt-bundle.js", ";\n");
  x497.add("", x496.readFileSync("/Users/naden/Developer/harana/harana/designer/js/target/scala-2.13/scalajs-bundler/main/designer-fastopt-library.js", "utf-8"), x496.readFileSync("/Users/naden/Developer/harana/harana/designer/js/target/scala-2.13/scalajs-bundler/main/designer-fastopt-library.js.map"));
  x497.add("designer-fastopt-loader.js", x496.readFileSync("/Users/naden/Developer/harana/harana/designer/js/target/scala-2.13/scalajs-bundler/main/designer-fastopt-loader.js"));
  x497.add("", x496.readFileSync("/Users/naden/Developer/harana/harana/designer/js/target/scala-2.13/scalajs-bundler/main/designer-fastopt.js", "utf-8"), x496.readFileSync("/Users/naden/Developer/harana/harana/designer/js/target/scala-2.13/scalajs-bundler/main/designer-fastopt.js.map", "utf-8"));
  var x498 = Buffer.from("\n//# sourceMappingURL=designer-fastopt-bundle.js.map\n");
  var x499 = Buffer.concat([x497.content, x498]);
  x496.writeFileSync("/Users/naden/Developer/harana/harana/designer/js/target/scala-2.13/scalajs-bundler/main/designer-fastopt-bundle.js", x499);
  x496.writeFileSync("/Users/naden/Developer/harana/harana/designer/js/target/scala-2.13/scalajs-bundler/main/designer-fastopt-bundle.js.map", x497.sourceMap)
}