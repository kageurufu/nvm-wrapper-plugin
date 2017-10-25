package org.jenkinsci.plugins.pyenv;

import hudson.EnvVars;
import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.tasks.BuildWrapper;
import hudson.tasks.BuildWrapperDescriptor;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;


  public class PyenvWrapper extends BuildWrapper {

  private final static Logger LOGGER = Logger.getLogger(PyenvWrapper.class.getName());

  private String version;
  private String pyenvInstallURL;
  private transient PyenvWrapperUtil wrapperUtil;

  @DataBoundConstructor
  public PyenvWrapper(String version, String pyenvInstallURL) {
    this.version = version;
    this.pyenvInstallURL = StringUtils.isNotBlank(pyenvInstallURL) ? pyenvInstallURL : PyenvDefaults.pyenvInstallURL;
  }

  public String getVersion() {
    return version;
  }

  public String getPyenvInstallURL() {
    return pyenvInstallURL;
  }

  @Override
  public BuildWrapper.Environment setUp(AbstractBuild build, Launcher launcher,final BuildListener listener)
    throws IOException, InterruptedException {
    this.wrapperUtil = new PyenvWrapperUtil(build.getWorkspace(), build.getParent().getName(), launcher, listener);
    final Map<String, String> pyenvEnvVars = this.wrapperUtil
      .getPyenvEnvVars(this.version, this.pyenvInstallURL);

    return new BuildWrapper.Environment() {
      @Override
      public void buildEnvVars(Map<String, String> env) {

        EnvVars envVars = new EnvVars(env);
        envVars.putAll(pyenvEnvVars);
        env.putAll(envVars);
      }
    };
  }

  @Extension
  public final static class DescriptorImpl extends BuildWrapperDescriptor {
    @Override
    public String getDisplayName() {
      return "Run the build in an Pyenv managed environment";
    }

    @Override
    public boolean isApplicable(AbstractProject<?, ?> item) {
      return true;
    }

  }

}
