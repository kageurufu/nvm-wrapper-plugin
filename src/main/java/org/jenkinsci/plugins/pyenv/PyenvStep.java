package org.jenkinsci.plugins.pyenv;

import com.google.common.collect.ImmutableSet;
import hudson.EnvVars;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.BuildListener;
import hudson.model.TaskListener;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.jenkinsci.plugins.workflow.steps.*;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Map;
import java.util.Set;


public class PyenvStep extends Step {

  private String version;
  private String pyenvInstallURL;

  @DataBoundConstructor
  public PyenvStep(final String version, final String pyenvInstallURL) {
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
  public StepExecution start(final StepContext context) throws Exception {
    return new Execution(this.version, this.pyenvInstallURL, context);
  }

  @Extension
  public static final class DescriptorImpl extends StepDescriptor {

    @Override
    public String getFunctionName() {
      return "pyenv";
    }

    @Override
    public String getDisplayName() {
      return "Setup the environment for an Pyenv installation.";
    }

    @Override
    public Set<? extends Class<?>> getRequiredContext() {
      return ImmutableSet.of(
        FilePath.class,
        Launcher.class,
        TaskListener.class
      );
    }

    @Override
    public Step newInstance(StaplerRequest req, JSONObject formData) throws FormException {
      final String versionFromFormData = formData.getString("version");
      final String pyenvInstallURLFromFormData = formData.getString("pyenvInstallURL");

      return new PyenvStep(versionFromFormData, pyenvInstallURLFromFormData);
    }

    @Override
    public boolean takesImplicitBlockArgument() {
      return true;
    }

  }

  public static class Execution extends AbstractStepExecutionImpl {

    private static final long serialVersionUID = 1;

    private final transient String version;
    private final transient String pyenvInstallURL;

    public Execution(final String version, final String pyenvInstallURL,
                     @Nonnull final StepContext context) {
      super(context);
      this.version = version;
      this.pyenvInstallURL = pyenvInstallURL;
    }

    @Override
    public boolean start() throws Exception {
      final FilePath workspace = this.getContext().get(FilePath.class);
      final Launcher launcher = this.getContext().get(Launcher.class);

      final PyenvWrapperUtil wrapperUtil = new PyenvWrapperUtil(workspace, launcher, launcher.getListener());
      final Map<String, String> pyenvEnvVars = wrapperUtil.getPyenvEnvVars(this.version, this.pyenvInstallURL);

      getContext().newBodyInvoker()
        .withContext(EnvironmentExpander.merge(getContext().get(EnvironmentExpander.class), new ExpanderImpl(pyenvEnvVars)))
        .withCallback(BodyExecutionCallback.wrap(getContext()))
        .start();

      return false;
    }

    @Override
    public void stop(@Nonnull Throwable cause) throws Exception {
      // No need to do anything heres
    }

  }

  private static class ExpanderImpl extends EnvironmentExpander {

    private final Map<String, String> envOverrides;

    public ExpanderImpl(final Map<String, String> envOverrides) {
      this.envOverrides = envOverrides;
    }

    @Override
    public void expand(@Nonnull final EnvVars env) throws IOException, InterruptedException {
      this.envOverrides.entrySet().forEach((entrySet) -> env.overrideAll(this.envOverrides));
    }

  }

}
