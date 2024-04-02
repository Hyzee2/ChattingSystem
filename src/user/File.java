package user;

import java.io.*;
import java.nio.file.*;
import java.util.stream.Stream;

public class File {
	public static void main(String[] args) {
		String directoryPath = "C:\\Users\\BIT\\Documents\\자바병합"; // 병합할 Java 파일들이 있는 디렉토리 경로
		String outputFilePath = "C:\\Users\\BIT\\Documents\\자바병합\\MergedJavaFiles.java"; // 병합된 파일의 출력 경로 및 이름

		// 출력 파일을 위한 PrintWriter 생성
		try (PrintWriter out = new PrintWriter(outputFilePath)) {
			// 지정된 디렉토리 내의 모든 파일을 리스트업
			try (Stream<Path> paths = Files.walk(Paths.get(directoryPath))) {
				paths.filter(Files::isRegularFile) // 디렉토리가 아닌 파일만 필터링
						.filter(path -> path.toString().endsWith(".java")) // .java 파일만 필터링
						.forEach(path -> {
							// 각 Java 파일의 내용을 읽고 출력 파일에 쓰기
							try (Stream<String> stream = Files.lines(path)) {
								out.println("// 파일: " + path.getFileName());
								stream.forEach(out::println);
								out.println(); // 파일 간 구분을 위해 빈 줄 추가
							} catch (IOException e) {
								e.printStackTrace();
							}
						});
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
