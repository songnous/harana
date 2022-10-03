package com.harana.sdk.backend.models.flow.actiontypes.readwritedataframe

import com.harana.sdk.backend.models.flow.ExecutionContext

object FilePathFromLibraryPath {

  def apply(path: FilePath)(implicit ctx: ExecutionContext): FilePath = {
    require(path.fileScheme == FileScheme.Library)
    val libraryPath = ctx.libraryPath + "/" + path.pathWithoutScheme
    FilePath(FileScheme.File, libraryPath)
  }
}
