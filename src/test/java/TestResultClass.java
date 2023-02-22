import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

public class TestResultClass implements TestWatcher, AfterAllCallback {
	private List<TestResultStatus> testResultsStatus = new ArrayList<>();

	private enum TestResultStatus {
		SUCCESSFUL, ABORTED, FAILED, DISABLED;
	}

	@Override
	public void testDisabled(ExtensionContext context, Optional<String> reason) {

		testResultsStatus.add(TestResultStatus.DISABLED);
	}

	@Override
	public void testSuccessful(ExtensionContext context) {

		testResultsStatus.add(TestResultStatus.SUCCESSFUL);
	}

	@Override
	public void testAborted(ExtensionContext context, Throwable cause) {

		testResultsStatus.add(TestResultStatus.ABORTED);
	}

	@Override
	public void testFailed(ExtensionContext context, Throwable cause) {
		testResultsStatus.add(TestResultStatus.FAILED);
	}

	@Override
	public void afterAll(ExtensionContext context) throws Exception {
		// TODO Auto-generated method stub
		Map<TestResultStatus, Long> summary = testResultsStatus.stream()
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
	}

}
