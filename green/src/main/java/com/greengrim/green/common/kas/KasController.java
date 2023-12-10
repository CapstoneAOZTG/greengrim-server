package com.greengrim.green.common.kas;

import java.io.IOException;
import java.text.ParseException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KasController {

  private final KasService kasService;

  @PostMapping("/asset")
  public void assetUpload()
      throws IOException, ParseException, org.json.simple.parser.ParseException, InterruptedException {
    kasService.uploadAsset();
  }
}
