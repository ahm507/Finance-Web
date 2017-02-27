package pf;

import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.failures.FailingUponPendingStep;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.junit.JUnitStory;
import org.jbehave.core.reporters.Format;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.InstanceStepsFactory;

public class BddBase extends JUnitStory {

    @Override
    public Configuration configuration() {
        MostUsefulConfiguration config = new MostUsefulConfiguration();

                config
                .useStoryLoader(new LoadFromClasspath(this.getClass().getClassLoader()))

                //If not matched or pending, Fail
                .usePendingStepStrategy(new FailingUponPendingStep())

//                .useStoryReporter(new SilentSuccessFilter(ConsoleOutput())
                
                .useStoryReporterBuilder(
                        new StoryReporterBuilder()
//generating reports fails frequently :( in a severly unclear error message
//                                .withDefaultFormats()
                                .withFormats(Format.CONSOLE)
//                                .withFormats(Format.HTML, Format.CONSOLE)
                        //.withRelativeDirectory("jbehave-bowling")
                                
                );

        //How do I output my story steps just when a story fails?
//        config.useStoryReporter(new SilentSuccessFilter(ConsoleOutput()));
        return config;
    }

//Deprecated
//    @Override
//    public List candidateSteps() {
//        return new InstanceStepsFactory(configuration(), this).createCandidateSteps();
//
//    }


    //Here we specify the steps classes as this
    @Override
    public InjectableStepsFactory stepsFactory() {
        // varargs, can have more that one steps classes
        return new InstanceStepsFactory(configuration(), this);
    }

    ////////////////////////////////
   
  
    
    

}
