package com.harana.sdk.backend.models.flow.actions

import com.harana.sdk.backend.models.flow.StandaloneSparkClusterForTests
import org.scalatest.BeforeAndAfterAll
import org.scalatest.Suites

class ClusterDependentSpecsSuite extends Suites(new InputOutputSpec) with BeforeAndAfterAll {

  override def beforeAll() = StandaloneSparkClusterForTests.startDockerizedCluster()

  override def afterAll() = StandaloneSparkClusterForTests.stopDockerizedCluster()

}
