package com.baulsupp.maven.script;

import java.io.File;
import java.io.IOException;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.FileUtils;

/**
 * @goal generate-env
 * @phase compile
 * @requiresDependencyResolution runtime 
 * @description generates a env script to set the classpath
 */
public class ScriptClasspathMojo extends AbstractMojo {
  /**
   * The environment variable to set.
   * @parameter expression="CP"
   */
  private String classpathVariable;
  
  /**     
   * @parameter expression="${project}"
   * @required
   * @readonly
   */
  private MavenProject project;
  
  /**     
   * @parameter expression="${project.build.directory}/env.sh"
   */
  private File outputFile;
  
  public void execute() throws MojoExecutionException, MojoFailureException {
    getLog().info("storing classpath " + classpathVariable + " to " + outputFile);
    
    try {
      StringBuilder sb = new StringBuilder("export ");
      sb.append(classpathVariable);
      sb.append("=");
      
//      sb.append(project.getExecutionProject().);
      
      for (Object o: project.getRuntimeClasspathElements()) {
        sb.append(':');
        sb.append(o);
      }
      
      FileUtils.fileWrite(outputFile.getAbsolutePath(), sb.toString());
    } catch (DependencyResolutionRequiredException e) {
      throw new MojoExecutionException("unexpected error", e);
    } catch (IOException e) {
      throw new MojoFailureException("unable to write to " + outputFile);
    }
  }
}
