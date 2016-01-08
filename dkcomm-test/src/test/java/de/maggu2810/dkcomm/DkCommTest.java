package de.maggu2810.dkcomm;

import static org.ops4j.pax.exam.CoreOptions.maven;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.configureConsole;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.features;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.karafDistributionConfiguration;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.keepRuntimeFolder;

import java.io.File;
import java.util.Enumeration;
import javax.comm.CommPortIdentifier;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.ConfigurationManager;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.options.MavenArtifactUrlReference;
import org.ops4j.pax.exam.options.MavenUrlReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(PaxExam.class)
public class DkCommTest {

  private final static Logger LOG = LoggerFactory.getLogger(DkCommTest.class);

  @Configuration
  public Option[] config() {

    MavenArtifactUrlReference karafUrl = maven()
            .groupId("org.apache.karaf")
            .artifactId("apache-karaf")
            .version(karafVersion())
            .type("zip");

    MavenUrlReference karafStandardRepo = maven()
            .groupId("org.apache.karaf.features")
            .artifactId("standard")
            .version(karafVersion())
            .classifier("features")
            .type("xml");

    return new Option[]{
      // KarafDistributionOption.debugConfiguration("5005", true),
      karafDistributionConfiguration().frameworkUrl(karafUrl).unpackDirectory(new File("target", "exam")).useDeployFolder(false),
      keepRuntimeFolder(),
      configureConsole().ignoreLocalConsole(),
      features(karafStandardRepo, "scr"),
      mavenBundle().groupId("de.maggu2810.dkcomm").artifactId("dkcomm").versionAsInProject().start()};
  }

  public static String karafVersion() {
    ConfigurationManager cm = new ConfigurationManager();
    String karafVersion = cm.getProperty("pax.exam.karaf.version", "4.0.3");
    return karafVersion;
  }

  @Test
  public void testIteration() {
    Enumeration<CommPortIdentifier> portList = CommPortIdentifier.getPortIdentifiers();

    while (portList.hasMoreElements()) {
      CommPortIdentifier portId = portList.nextElement();
      LOG.info("port: type={}, name={}", portId.getPortType(), portId.getName());
    }
  }

}
