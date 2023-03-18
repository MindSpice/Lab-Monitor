package io.mindspice.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.mindspice.state.ClientStates;
import io.mindspice.state.NutState;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class Dashboard {
    public static final ObjectMapper json = new ObjectMapper();


    @GetMapping("/nut_full")
    public ResponseEntity<String> nutFull() {
        var data = NutState.get().getFullData();
        try {
            if (data == null) {
                throw new IllegalStateException("Failed To Fetch Full Nut Data");
            }
            return new ResponseEntity<>(json.writeValueAsString(data), HttpStatus.OK);
        } catch (JsonProcessingException e) {
            System.out.println("Failed To Serialize Nut Data");
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }


    @GetMapping("/nut_overview")
    public ResponseEntity<String> nutSimple() {
        var data = NutState.get().getRecentData();
        try {
            if (data == null) {
                throw new IllegalStateException("Failed To Fetch Simple Nut Data");
            }
            return new ResponseEntity<>(json.writeValueAsString(data), HttpStatus.OK);
        } catch (JsonProcessingException e) {
            System.out.println("Failed To Serialize Nut Data");
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }


    @GetMapping("/nut_info_list")
    public ResponseEntity<String> nutInfo() {
        var node = json.createObjectNode();
        try {
            var data = NutState.get().getInfoList();
            if (data == null) {
                throw new IllegalStateException("Failed To Fetch Nut Info List");
            }
            node.putPOJO("info", data);
            return new ResponseEntity<>(json.writeValueAsString(data), HttpStatus.OK);
        } catch (JsonProcessingException e) {
            System.out.println("Failed To Serialize Nut Data");
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }


    @GetMapping("/client_overview")
    public ResponseEntity<String> clientsOverview() {
        var data = ClientStates.get().getClientsOverview();
        try {
            if (data == null || data.isEmpty()) {
                throw new IllegalStateException("Failed To Fetch Client Overview");
            }
            return new ResponseEntity<>(json.writeValueAsString(data), HttpStatus.OK);
        } catch (JsonProcessingException e) {
            System.out.println("Failed To Serialize Client Overview");
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }


    @GetMapping("/client_full")
    public ResponseEntity<String> clientFull(String name) {
        var data = ClientStates.get().getClientData(name);
        try {
            if (data == null) {
                throw new IllegalStateException("Failed To Fetch Client Data For: " + name);
            }
            return new ResponseEntity<>(json.writeValueAsString(data), HttpStatus.OK);
        } catch (JsonProcessingException e) {
            System.out.println("Failed To Serialize Client Overview For" + name);
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }
}
