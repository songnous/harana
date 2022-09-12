package com.harana.designer.frontend.utils

import com.harana.shared.models.HaranaFile

object FileUtils {

    case class Language(name: String, extensions: List[String], overrideStyle: Option[String] = None)

    def parent(path: String): String = {
        val items = path.split("/").dropRight(1)
        if (items.length > 1) items.mkString("/") else "/"
    }

    def join(paths: String*): String =
        "/" + paths.filter(_.nonEmpty).filterNot(_.equals("/")).map(_.trim.replace("/", "")).mkString("/")


    def isArchive(file: HaranaFile): Boolean =
        List("zip", "tgz", "tar", "bz2", "gz").contains(file.`extension`.getOrElse(""))


    def isCode(file: HaranaFile): Boolean = {
        val extension = file.`extension`.getOrElse("")
        FileUtils.languages.flatMap(_.extensions).contains(extension)
    }

    def isImage(file: HaranaFile) : Boolean =
        List("gif", "jpg", "jpeg", "png").contains(file.`extension`.getOrElse(""))


    def isTabular(file: HaranaFile) : Boolean =
        List("avro", "csv", "parquet", "orc").contains(file.`extension`.getOrElse(""))


    def highlightType(file: HaranaFile): String =
        if (file.`extension`.isDefined) {
            val language = languages.filter(l => l.extensions.contains(file.`extension`.get))
            if (language.isEmpty) "plaintext" else {
                if (language.head.overrideStyle.isDefined)
                    language.head.overrideStyle.get
                else
                    language.head.extensions.head.toLowerCase
            }
        } else "plaintext"


    val languages = List(
        Language("1c", List("1c")),
        Language("4d", List("4d"), Some("highlightjs-4d")),
        Language("abnf", List("abnf")),
        Language("access logs", List("accesslog")),
        Language("ada", List("ada")),
        Language("arduino (c++ w/arduino libs)", List("arduino", "ino")),
        Language("arm assembler", List("armasm", "arm")),
        Language("avr assembler", List("avrasm")),
        Language("actionscript", List("actionscript", "as")),
        Language("alan if", List("alan", "i"), Some("highlightjs-alan")),
        Language("alan", List("ln"), Some("highlightjs-alan")),
        Language("angelscript", List("angelscript", "asc")),
        Language("apache", List("apache", "apacheconf")),
        Language("applescript", List("applescript", "osascript")),
        Language("arcade", List("arcade")),
        Language("asciidoc", List("asciidoc", "adoc")),
        Language("aspectj", List("aspectj")),
        Language("autohotkey", List("autohotkey")),
        Language("autoit", List("autoit")),
        Language("awk", List("awk", "mawk", "nawk", "gawk")),
        Language("bash", List("bash", "sh", "zsh")),
        Language("basic", List("basic")),
        Language("bnf", List("bnf")),
        Language("brainfuck", List("brainfuck", "bf")),
        Language("c#", List("csharp", "cs")),
        Language("c", List("c", "h")),
        Language("c++", List("cpp", "hpp", "cc", "hh", "c++", "h++", "cxx", "hxx")),
        Language("c/al", List("cal")),
        Language("cache object script", List("cos", "cls")),
        Language("cmake", List("cmake", "cmake.in")),
        Language("coq", List("coq")),
        Language("csp", List("csp")),
        Language("css", List("css")),
        Language("cap’n proto", List("capnproto", "capnp")),
        Language("chaos", List("chaos", "kaos"), Some("highlightjs-chaos")),
        Language("cisco cli", List("cisco"), Some("highlightjs-cisco-cli")),
        Language("clojure", List("clojure", "clj")),
        Language("coffeescript", List("coffeescript", "coffee", "cson", "iced")),
        Language("cpcdosc+", List("cpc"), Some("highlightjs-cpcdos")),
        Language("crmsh", List("crmsh", "crm", "pcmk")),
        Language("crystal", List("crystal", "cr")),
        Language("neo4j cypher", List("cypher"), Some("highlightjs-cypher")),
        Language("d", List("d")),
        Language("dns zone file", List("dns", "zone", "bind")),
        Language("dos", List("dos", "bat", "cmd")),
        Language("dart", List("dart")),
        Language("delphi", List("delphi", "dpr", "dfm", "pas", "pascal", "freepascal", "lazarus", "lpr", "lfm")),
        Language("diff", List("diff", "patch")),
        Language("django", List("django", "jinja")),
        Language("dockerfile", List("dockerfile", "docker")),
        Language("dsconfig", List("dsconfig")),
        Language("dts (device tree)", List("dts")),
        Language("dust", List("dust", "dst")),
        Language("dylan", List("dylan"), Some("highlight-dylan")),
        Language("ebnf", List("ebnf")),
        Language("elixir", List("elixir")),
        Language("elm", List("elm")),
        Language("erlang", List("erlang", "erl")),
        Language("excel", List("excel", "xls", "xlsx")),
        Language("extempore", List("extempore", "xtlang", "xtm"), Some("highlightjs-xtlang")),
        Language("f#", List("fsharp", "fs")),
        Language("fix", List("fix")),
        Language("fortran", List("fortran", "f90", "f95")),
        Language("g-code", List("gcode", "nc")),
        Language("gams", List("gams", "gms")),
        Language("gauss", List("gauss", "gss")),
        Language("gdscript", List("godot", "gdscript"), Some("highlightjs-gdscript")),
        Language("gherkin", List("gherkin")),
        Language("gn for ninja", List("gn", "gni"), Some("highlightjs-gn")),
        Language("go", List("go", "golang")),
        Language("grammatical framework", List("gf"), Some("highlightjs-gf")),
        Language("golo", List("golo", "gololang")),
        Language("gradle", List("gradle")),
        Language("groovy", List("groovy")),
        Language("html", List("html", "xhtml"), Some("xml")),
        Language("http", List("http", "https")),
        Language("haml", List("haml")),
        Language("handlebars", List("handlebars", "hbs", "html.hbs", "html.handlebars")),
        Language("haskell", List("haskell", "hs")),
        Language("haxe", List("haxe", "hx")),
        Language("hy", List("hy", "hylang")),
        Language("ini", List("ini")),
        Language("inform7", List("inform7", "i7")),
        Language("irpf90", List("irpf90")),
        Language("json", List("json")),
        Language("java", List("java", "jsp")),
        Language("javascript", List("javascript", "js", "jsx")),
        Language("jolie", List("jolie", "iol", "ol"), Some("highlightjs-jolie")),
        Language("kotlin", List("kotlin", "kt")),
        Language("latex", List("tex")),
        Language("leaf", List("leaf")),
        Language("lean", List("lean"), Some("highlightjs-lean")),
        Language("lasso", List("lasso", "ls", "lassoscript")),
        Language("less", List("less")),
        Language("ldif", List("ldif")),
        Language("lisp", List("lisp")),
        Language("livecode server", List("livecodeserver")),
        Language("livescript", List("livescript", "ls")),
        Language("lua", List("lua")),
        Language("makefile", List("makefile", "mk", "mak")),
        Language("markdown", List("markdown", "md", "mkdown", "mkd")),
        Language("mathematica", List("mathematica", "mma", "wl")),
        Language("matlab", List("matlab")),
        Language("maxima", List("maxima")),
        Language("maya embedded language", List("mel")),
        Language("mercury", List("mercury")),
        Language("mizar", List("mizar")),
        Language("mojolicious", List("mojolicious")),
        Language("monkey", List("monkey")),
        Language("moonscript", List("moonscript", "moon")),
        Language("n1ql", List("n1ql")),
        Language("nsis", List("nsis")),
        Language("never", List("never"), Some("highlightjs-never")),
        Language("nginx", List("nginx", "nginxconf")),
        Language("nim", List("nim", "nimrod")),
        Language("nix", List("nix")),
        Language("object constraint language", List("ocl"), Some("highlightjs-ocl")),
        Language("ocaml", List("ocaml", "ml")),
        Language("objective c", List("objectivec", "mm", "objc", "obj-c", "obj-c++", "objective-c++")),
        Language("opengl shading language", List("glsl")),
        Language("openscad", List("openscad", "scad")),
        Language("oracle rules language", List("ruleslanguage")),
        Language("oxygene", List("oxygene")),
        Language("pf", List("pf", "pf.conf")),
        Language("php", List("php", "php3", "php4", "php5", "php6", "php7")),
        Language("parser3", List("parser3")),
        Language("perl", List("perl", "pl", "pm")),
        Language("plaintext", List("plaintext", "txt", "text")),
        Language("plist", List("plist"), Some("xml")),
        Language("pony", List("pony")),
        Language("postgresql & pl/pgsql", List("pgsql", "postgres", "postgresql")),
        Language("powershell", List("powershell", "ps", "ps1")),
        Language("processing", List("processing")),
        Language("prolog", List("prolog")),
        Language("properties", List("properties")),
        Language("protocol buffers", List("protobuf")),
        Language("puppet", List("puppet", "pp")),
        Language("python", List("python", "py", "gyp")),
        Language("python profiler results", List("profile")),
        Language("python repl", List("python-repl", "pycon")),
        Language("q", List("k", "kdb")),
        Language("qml", List("qml")),
        Language("r", List("r")),
        Language("razor cshtml", List("cshtml", "razor", "razor-cshtml"), Some("highlightjs-cshtml-razor")),
        Language("reasonml", List("reasonml", "re")),
        Language("renderman rib", List("rib")),
        Language("renderman rsl", List("rsl")),
        Language("roboconf", List("graph", "instances")),
        Language("robot framework", List("robot", "rf"), Some("highlightjs-robot")),
        Language("rpm spec files", List("rpm-specfile", "rpm", "spec", "rpm-spec", "specfile"), Some("highlightjs-rpm-specfile")),
        Language("rss", List("rss", "atom"), Some("xml")),
        Language("ruby", List("ruby", "rb", "gemspec", "podspec", "thor", "irb")),
        Language("rust", List("rust", "rs")),
        Language("sas", List("sas", "sas")),
        Language("scss", List("scss")),
        Language("sql", List("sql")),
        Language("step part 21", List("p21", "step", "stp")),
        Language("scala", List("scala", "sbt")),
        Language("scheme", List("scheme")),
        Language("scilab", List("scilab", "sci")),
        Language("shape expressions", List("shexc"), Some("highlightjs-shexc")),
        Language("shell", List("shell", "console")),
        Language("smali", List("smali")),
        Language("smalltalk", List("smalltalk", "st")),
        Language("sml", List("sml", "ml")),
        Language("solidity", List("solidity", "sol"), Some("highlightjs-solidity")),
        Language("stan", List("stan", "stanfuncs")),
        Language("stata", List("stata")),
        Language("structured text", List("iecst", "scl", "stl", "structured-text"), Some("highlightjs-structured-text")),
        Language("stylus", List("stylus", "styl")),
        Language("subunit", List("subunit")),
        Language("supercollider", List("supercollider", "sc"), Some("highlightjs-supercollider")),
        Language("svelte", List("svelte"), Some("highlightjs-svelte")),
        Language("svg", List("svg"), Some("xml")),
        Language("swift", List("swift")),
        Language("tcl", List("tcl", "tk")),
        Language("terraform", List("terraform", "tf", "hcl"), Some("highlightjs-terraform")),
        Language("test anything protocol", List("tap")),
        Language("thrift", List("thrift")),
        Language("toml", List("toml"), Some("ini")),
        Language("tp", List("tp")),
        Language("transact-sql", List("tsql"), Some("highlightjs-tsql")),
        Language("twig", List("twig", "craftcms")),
        Language("typescript", List("typescript", "ts")),
        Language("unicorn rails log", List("unicorn-rails-log"), Some("highlightjs-unicorn-rails")),
        Language("vb.net", List("vbnet", "vb")),
        Language("vba", List("vba"), Some("highlightjs-vba")),
        Language("vbscript", List("vbscript", "vbs")),
        Language("vhdl", List("vhdl")),
        Language("vala", List("vala")),
        Language("verilog", List("verilog", "v")),
        Language("vim script", List("vim")),
        Language("x++", List("axapta", "x++")),
        Language("x86 assembly", List("x86asm")),
        Language("xl", List("xl", "tao")),
        Language("xml", List("xml")),
        Language("xsd", List("xsd", "xsl"), Some("xml")),
        Language("xquery", List("xquery", "xpath", "xq")),
        Language("yaml", List("yml", "yaml")),
        Language("zephir", List("zephir", "zep"))

    )
}