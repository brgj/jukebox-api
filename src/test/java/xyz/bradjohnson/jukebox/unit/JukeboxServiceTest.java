package xyz.bradjohnson.jukebox.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import xyz.bradjohnson.jukebox.entity.Jukebox;
import xyz.bradjohnson.jukebox.entity.Settings;
import xyz.bradjohnson.jukebox.repository.JukeboxRepository;
import xyz.bradjohnson.jukebox.repository.SettingsRepository;
import xyz.bradjohnson.jukebox.service.JukeboxService;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.Collections;

public class JukeboxServiceTest {

    private static ObjectMapper objectMapper;
    private static JukeboxService jukeboxService;

    @BeforeAll
    public static void before() {
        final WebTarget mockJukeboxTarget = Mockito.mock(WebTarget.class);
        final WebTarget mockSettingsTarget = Mockito.mock(WebTarget.class);
        final Invocation.Builder mockJukeboxBuilder = Mockito.mock(Invocation.Builder.class);
        final Invocation.Builder mockSettingsBuilder = Mockito.mock(Invocation.Builder.class);
        objectMapper = new ObjectMapper();

        Mockito.when(mockJukeboxTarget.request(MediaType.APPLICATION_JSON))
                .then(mock -> mockJukeboxBuilder);
        Mockito.when(mockSettingsTarget.request(MediaType.APPLICATION_JSON))
                .then(mock -> mockSettingsBuilder);

        Mockito.when(mockJukeboxBuilder.get(Jukebox[].class))
                .then(mock -> getJsonJukeData());
        Mockito.when(mockSettingsBuilder.get(Settings.SuperSettings.class))
                .then(mock -> getJsonSettingsData());

        jukeboxService = new JukeboxService(
                new JukeboxRepository(mockJukeboxTarget),
                new SettingsRepository(mockSettingsTarget));
    }

    @Test
    public void filterBySettingIdTest() {
        final String settingsId = "b43f247a-8478-4f24-8e28-792fcfe539f5";

        final Jukebox expectedJukebox = new Jukebox();
        final Jukebox.Component expectedComponent1 = new Jukebox.Component();
        final Jukebox.Component expectedComponent2 = new Jukebox.Component();

        expectedComponent1.setName("camera");
        expectedComponent2.setName("amplifier");

        expectedJukebox.setComponents(new Jukebox.Component[]{expectedComponent1, expectedComponent2});
        expectedJukebox.setId("5ca94a8ab592da6c6f2d562e");
        expectedJukebox.setModel("fusion");

        Assertions.assertThat(jukeboxService.getJukeboxes(settingsId, null, 0L, 20L))
                .isEqualTo(Collections.singletonList(expectedJukebox));
    }

    @Test
    public void filterByModelTest() {
        final String settingsId = "67ab1ec7-59b8-42f9-b96c-b261cc2a2ed9";
        final String model = "fusion";

        final Jukebox expectedJukebox = new Jukebox();
        final Jukebox.Component expectedComponent1 = new Jukebox.Component();
        final Jukebox.Component expectedComponent2 = new Jukebox.Component();
        final Jukebox.Component expectedComponent3 = new Jukebox.Component();
        final Jukebox.Component expectedComponent4 = new Jukebox.Component();
        final Jukebox.Component expectedComponent5 = new Jukebox.Component();

        expectedComponent1.setName("led_panel");
        expectedComponent2.setName("amplifier");
        expectedComponent3.setName("led_panel");
        expectedComponent4.setName("led_panel");
        expectedComponent5.setName("pcb");

        expectedJukebox.setComponents(new Jukebox.Component[]{
                expectedComponent1,
                expectedComponent2,
                expectedComponent3,
                expectedComponent4,
                expectedComponent5});
        expectedJukebox.setId("5ca94a8ac470d3e47cd4713c");
        expectedJukebox.setModel("fusion");

        Assertions.assertThat(jukeboxService.getJukeboxes(settingsId, model, 0L, 20L))
                .isEqualTo(Collections.singletonList(expectedJukebox));
    }

    @Test
    public void getCountWithOffsetAndLimitTest() {
        final String settingsId = "18beae74-a24f-425d-b9d2-8d9d69be89fa";

        Assertions.assertThat(jukeboxService.getJukeboxes(settingsId, null, 0L, 99L).size())
                .isEqualTo(30);

        Assertions.assertThat(jukeboxService.getJukeboxes(settingsId, null, 5L, 99L).size())
                .isEqualTo(25);

        Assertions.assertThat(jukeboxService.getJukeboxes(settingsId, null, 5L, 15L).size())
                .isEqualTo(15);
    }

    private static Jukebox[] getJsonJukeData() throws IOException {
        return objectMapper.readerFor(Jukebox[].class).readValue("[\n" +
                "  {\n" +
                "    \"id\": \"5ca94a8ac470d3e47cd4713c\",\n" +
                "    \"model\": \"fusion\",\n" +
                "    \"components\": [\n" +
                "      {\n" +
                "        \"name\": \"led_panel\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"amplifier\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"led_panel\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"led_panel\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"pcb\"\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"5ca94a8a77e20d15a7d16d0a\",\n" +
                "    \"model\": \"angelina\",\n" +
                "    \"components\": [\n" +
                "      {\n" +
                "        \"name\": \"pcb\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"money_pcb\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"touchscreen\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"speaker\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"speaker\"\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"5ca94a8a75c231bb18715112\",\n" +
                "    \"model\": \"fusion\",\n" +
                "    \"components\": [\n" +
                "      {\n" +
                "        \"name\": \"amplifier\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"led_panel\"\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"5ca94a8a20905ffff6f0561c\",\n" +
                "    \"model\": \"virtuo\",\n" +
                "    \"components\": [\n" +
                "      {\n" +
                "        \"name\": \"led_panel\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"money_receiver\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"money_receiver\"\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"5ca94a8a3227b0a360f41078\",\n" +
                "    \"model\": \"fusion\",\n" +
                "    \"components\": []\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"5ca94a8ac3f21c47a72473ec\",\n" +
                "    \"model\": \"virtuo\",\n" +
                "    \"components\": [\n" +
                "      {\n" +
                "        \"name\": \"led_panel\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"pcb\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"amplifier\"\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"5ca94a8ad82e60f2448d2fc9\",\n" +
                "    \"model\": \"angelina\",\n" +
                "    \"components\": [\n" +
                "      {\n" +
                "        \"name\": \"speaker\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"touchscreen\"\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"5ca94a8ab592da6c6f2d562e\",\n" +
                "    \"model\": \"fusion\",\n" +
                "    \"components\": [\n" +
                "      {\n" +
                "        \"name\": \"camera\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"amplifier\"\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"5ca94a8ac5f85b8a59f9e3c8\",\n" +
                "    \"model\": \"virtuo\",\n" +
                "    \"components\": [\n" +
                "      {\n" +
                "        \"name\": \"money_pcb\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"camera\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"camera\"\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"5ca94a8a13385f0c82aa9f2e\",\n" +
                "    \"model\": \"virtuo\",\n" +
                "    \"components\": [\n" +
                "      {\n" +
                "        \"name\": \"money_storage\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"pcb\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"touchscreen\"\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"5ca94a8aafb9d8c4e4fddf02\",\n" +
                "    \"model\": \"angelina\",\n" +
                "    \"components\": []\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"5ca94a8a1639eb9ea30609f0\",\n" +
                "    \"model\": \"virtuo\",\n" +
                "    \"components\": []\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"5ca94a8a4aeb7ab33a5e1047\",\n" +
                "    \"model\": \"virtuo\",\n" +
                "    \"components\": [\n" +
                "      {\n" +
                "        \"name\": \"money_pcb\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"camera\"\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"5ca94a8ad2d584257d25ae50\",\n" +
                "    \"model\": \"fusion\",\n" +
                "    \"components\": [\n" +
                "      {\n" +
                "        \"name\": \"money_receiver\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"money_receiver\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"money_pcb\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"amplifier\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"led_panel\"\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"5ca94a8a8b58770bb38055a0\",\n" +
                "    \"model\": \"angelina\",\n" +
                "    \"components\": [\n" +
                "      {\n" +
                "        \"name\": \"money_storage\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"pcb\"\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"5ca94a8afa2bc9887b28ce87\",\n" +
                "    \"model\": \"angelina\",\n" +
                "    \"components\": [\n" +
                "      {\n" +
                "        \"name\": \"amplifier\"\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"5ca94a8adb81479f94dda744\",\n" +
                "    \"model\": \"fusion\",\n" +
                "    \"components\": [\n" +
                "      {\n" +
                "        \"name\": \"led_matrix\"\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"5ca94a8a0735998f945f7276\",\n" +
                "    \"model\": \"fusion\",\n" +
                "    \"components\": [\n" +
                "      {\n" +
                "        \"name\": \"touchscreen\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"camera\"\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"5ca94a8a59b8061f89644f43\",\n" +
                "    \"model\": \"fusion\",\n" +
                "    \"components\": [\n" +
                "      {\n" +
                "        \"name\": \"speaker\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"amplifier\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"touchscreen\"\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"5ca94a8acc046e7aa8040605\",\n" +
                "    \"model\": \"angelina\",\n" +
                "    \"components\": [\n" +
                "      {\n" +
                "        \"name\": \"money_receiver\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"led_matrix\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"speaker\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"touchscreen\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"money_pcb\"\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"5ca94a8af0853f96c44fa858\",\n" +
                "    \"model\": \"virtuo\",\n" +
                "    \"components\": [\n" +
                "      {\n" +
                "        \"name\": \"led_matrix\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"touchscreen\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"money_storage\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"pcb\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"money_receiver\"\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"5ca94a8a18f5576210fd012e\",\n" +
                "    \"model\": \"virtuo\",\n" +
                "    \"components\": []\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"5ca94a8ae2b3a4fb2f0cfd78\",\n" +
                "    \"model\": \"fusion\",\n" +
                "    \"components\": [\n" +
                "      {\n" +
                "        \"name\": \"led_matrix\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"money_pcb\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"money_receiver\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"led_matrix\"\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"5ca94a8acfdeb5e01e5bdbe8\",\n" +
                "    \"model\": \"virtuo\",\n" +
                "    \"components\": [\n" +
                "      {\n" +
                "        \"name\": \"money_storage\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"money_pcb\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"money_storage\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"camera\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"money_receiver\"\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"5ca94a8a2c516506b1f49500\",\n" +
                "    \"model\": \"angelina\",\n" +
                "    \"components\": [\n" +
                "      {\n" +
                "        \"name\": \"money_pcb\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"led_matrix\"\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"5ca94a8af9985926172d6e8d\",\n" +
                "    \"model\": \"angelina\",\n" +
                "    \"components\": [\n" +
                "      {\n" +
                "        \"name\": \"speaker\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"led_matrix\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"money_pcb\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"pcb\"\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"5ca94a8a1d1bc6d59afb9392\",\n" +
                "    \"model\": \"virtuo\",\n" +
                "    \"components\": [\n" +
                "      {\n" +
                "        \"name\": \"money_storage\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"speaker\"\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"5ca94a8aa2330a0762019ac0\",\n" +
                "    \"model\": \"angelina\",\n" +
                "    \"components\": [\n" +
                "      {\n" +
                "        \"name\": \"money_storage\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"amplifier\"\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"5ca94a8ab2c1285e53a89991\",\n" +
                "    \"model\": \"fusion\",\n" +
                "    \"components\": []\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"5ca94a8a72473ac501b99033\",\n" +
                "    \"model\": \"angelina\",\n" +
                "    \"components\": []\n" +
                "  }\n" +
                "]");
    }

    private static Settings.SuperSettings getJsonSettingsData() throws IOException {
        return objectMapper.readerFor(Settings.SuperSettings.class).readValue("{\n" +
                "  \"settings\": [\n" +
                "    {\n" +
                "      \"id\": \"e9869bbe-887f-4d0a-bb9d-b81eb55fbf0a\",\n" +
                "      \"requires\": [\n" +
                "        \"camera\",\n" +
                "        \"speaker\",\n" +
                "        \"pcb\"\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"db886f37-16e3-4a55-80e4-cfffc9e4e464\",\n" +
                "      \"requires\": [\n" +
                "        \"touchscreen\",\n" +
                "        \"money_pcb\",\n" +
                "        \"led_panel\",\n" +
                "        \"money_receiver\"\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"2321763c-8e06-4a31-873d-0b5dac2436da\",\n" +
                "      \"requires\": [\n" +
                "        \"touchscreen\",\n" +
                "        \"money_pcb\"\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"2f58dbd4-47cb-4eef-bb72-623f4aa4fe5d\",\n" +
                "      \"requires\": [\n" +
                "        \"money_pcb\",\n" +
                "        \"money_receiver\",\n" +
                "        \"speaker\",\n" +
                "        \"led_panel\",\n" +
                "        \"money_receiver\"\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"b43f247a-8478-4f24-8e28-792fcfe539f5\",\n" +
                "      \"requires\": [\n" +
                "        \"camera\",\n" +
                "        \"amplifier\"\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"86506865-f971-496e-9b90-75994f251459\",\n" +
                "      \"requires\": []\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"3a6423cf-f226-4cb1-bf51-2954bc0941d1\",\n" +
                "      \"requires\": [\n" +
                "        \"speaker\",\n" +
                "        \"money_receiver\"\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"fda6c028-ca52-418c-ac91-574228c171c5\",\n" +
                "      \"requires\": [\n" +
                "        \"money_storage\",\n" +
                "        \"speaker\",\n" +
                "        \"money_storage\"\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"515ef38b-0529-418f-a93a-7f2347fc5805\",\n" +
                "      \"requires\": [\n" +
                "        \"money_storage\"\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"1b9ad45f-8c0e-4993-b980-377ac19b121e\",\n" +
                "      \"requires\": [\n" +
                "        \"amplifier\",\n" +
                "        \"pcb\",\n" +
                "        \"money_receiver\",\n" +
                "        \"money_storage\"\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"bd9df656-323c-4417-b14b-bd9e9743be23\",\n" +
                "      \"requires\": [\n" +
                "        \"money_pcb\",\n" +
                "        \"led_panel\",\n" +
                "        \"money_pcb\",\n" +
                "        \"touchscreen\",\n" +
                "        \"speaker\"\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"aae445bf-72f0-4680-a23e-18fcf7241f8b\",\n" +
                "      \"requires\": []\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"76b7f0b8-eb8e-4156-a6cb-43c6b421c69e\",\n" +
                "      \"requires\": [\n" +
                "        \"camera\",\n" +
                "        \"led_matrix\"\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"ae209984-dc64-406b-bc95-3b6153cdbaa6\",\n" +
                "      \"requires\": [\n" +
                "        \"camera\",\n" +
                "        \"pcb\",\n" +
                "        \"led_matrix\"\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"f600ede7-0709-4ca0-b95d-95a5315b9385\",\n" +
                "      \"requires\": []\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"8976a660-f990-46e3-a698-ff8fbb887d46\",\n" +
                "      \"requires\": [\n" +
                "        \"pcb\",\n" +
                "        \"pcb\",\n" +
                "        \"money_receiver\",\n" +
                "        \"touchscreen\",\n" +
                "        \"camera\"\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"922a8e2b-6278-498a-a54a-fd92206ace67\",\n" +
                "      \"requires\": []\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"616e0c31-92ea-4896-8487-62e1b5336aca\",\n" +
                "      \"requires\": [\n" +
                "        \"money_receiver\",\n" +
                "        \"pcb\",\n" +
                "        \"led_panel\",\n" +
                "        \"pcb\"\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"18beae74-a24f-425d-b9d2-8d9d69be89fa\",\n" +
                "      \"requires\": []\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"67ab1ec7-59b8-42f9-b96c-b261cc2a2ed9\",\n" +
                "      \"requires\": [\n" +
                "        \"pcb\"\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"2709ac80-2593-4dcf-ae72-a3c2fac023a5\",\n" +
                "      \"requires\": [\n" +
                "        \"pcb\",\n" +
                "        \"money_storage\",\n" +
                "        \"led_matrix\",\n" +
                "        \"speaker\",\n" +
                "        \"pcb\"\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"7e57cfef-7267-4434-b882-a37ac3d76034\",\n" +
                "      \"requires\": [\n" +
                "        \"money_pcb\",\n" +
                "        \"touchscreen\",\n" +
                "        \"money_pcb\"\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"daab00f8-b10e-48f2-bca6-494c5a2869aa\",\n" +
                "      \"requires\": [\n" +
                "        \"touchscreen\",\n" +
                "        \"money_receiver\",\n" +
                "        \"speaker\",\n" +
                "        \"led_panel\",\n" +
                "        \"money_receiver\"\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"a794498e-ce14-499e-8bf6-7d09bda6d3c5\",\n" +
                "      \"requires\": [\n" +
                "        \"money_pcb\",\n" +
                "        \"money_pcb\",\n" +
                "        \"pcb\",\n" +
                "        \"camera\"\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"207797de-5857-4c60-a69b-80eea28bcce8\",\n" +
                "      \"requires\": [\n" +
                "        \"pcb\"\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"acbb6362-a6c2-40ce-9ee7-6504816e24d7\",\n" +
                "      \"requires\": [\n" +
                "        \"money_pcb\",\n" +
                "        \"led_matrix\",\n" +
                "        \"amplifier\"\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"358a044e-decc-47cc-aaf1-e2253a00998e\",\n" +
                "      \"requires\": []\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"9ac2d388-0f1b-4137-8415-02b953dd76f7\",\n" +
                "      \"requires\": [\n" +
                "        \"camera\",\n" +
                "        \"money_receiver\"\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"ba4ecdf7-11fa-4d05-8d4b-67b26c991cb0\",\n" +
                "      \"requires\": [\n" +
                "        \"camera\",\n" +
                "        \"led_matrix\",\n" +
                "        \"touchscreen\",\n" +
                "        \"camera\"\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"4efdf86e-68a1-4256-a154-5069510d78fc\",\n" +
                "      \"requires\": [\n" +
                "        \"speaker\",\n" +
                "        \"camera\",\n" +
                "        \"led_panel\"\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}");
    }
}
